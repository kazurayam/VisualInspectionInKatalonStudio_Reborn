package com.kazurayam.ks.visualinspection

import java.nio.file.Files
import java.nio.file.Path

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.uitestjava.flaskr.pom.actions.ActionListener
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class StoreMaterialActionListener extends ActionListener {

	public static final NULL_OBJECT =
	new StoreMaterialActionListener("__NULL_OBJECT__", Store.NULL_OBJECT, JobName.NULL_OBJECT, JobTimestamp.NULL_OBJECT)

	private final String profile
	private final Store store
	private final JobName jobName
	private final JobTimestamp jobTimestamp

	StoreMaterialActionListener(String profile, Store store, JobName jobName, JobTimestamp jobTimestamp) {
		Objects.requireNonNull(profile)
		Objects.requireNonNull(store)
		Objects.requireNonNull(jobName)
		Objects.requireNonNull(jobTimestamp)
		this.profile = profile
		this.store = store
		this.jobName = jobName
		this.jobTimestamp = jobTimestamp
	}

	@Override
	void on(Class clazz, URL url, Map<String, String> attributes) {
		Objects.requireNonNull(url)
		Objects.requireNonNull(attributes)
		Metadata metadata = Metadata.builderWithUrl(url)
				.put("profile", profile)
				.putAll(attributes)
				.build()

		// take a screenshot and save the image into a temporary file using Katalon's built-in keyword
		Path tempFile = Files.createTempFile(null, null);
		WebUI.takeFullPageScreenshot(tempFile.toAbsolutePath().toFile().toString(), [])

		// copy the image file into the materialstore
		Material image = store.write(jobName, jobTimestamp, FileType.PNG, metadata, tempFile)
		assert image != null

		// save the page source HTML into the materialstore
		WebDriver driver = DriverFactory.getWebDriver()
		Document doc = Jsoup.parse(driver.getPageSource())
		Material html = store.write(jobName, jobTimestamp, FileType.HTML, metadata, doc.toString())
		assert html != null
	}
}
