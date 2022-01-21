package com.kazurayam.ks.visualinspection

import java.nio.file.Files
import java.nio.file.Path

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.FileType
import com.kazurayam.materialstore.JobName
import com.kazurayam.materialstore.JobTimestamp
import com.kazurayam.materialstore.Material
import com.kazurayam.materialstore.Metadata
import com.kazurayam.materialstore.Store
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class MaterializingContext {

	public static final NULL_OBJECT =
	new MaterializingContext("__NULL_OBJECT__", Store.NULL_OBJECT, JobName.NULL_OBJECT, JobTimestamp.NULL_OBJECT)

	private final String profile
	private final Store store
	private final JobName jobName
	private final JobTimestamp jobTimestamp

	MaterializingContext(String profile, Store store, JobName jobName, JobTimestamp jobTimestamp) {
		Objects.requireNonNull(profile)
		Objects.requireNonNull(store)
		Objects.requireNonNull(jobName)
		Objects.requireNonNull(jobTimestamp)
		this.profile = profile
		this.store = store
		this.jobName = jobName
		this.jobTimestamp = jobTimestamp
	}

	Tuple materialize(URL url) {
		return materialize(url, [:])
	}

	Tuple materialize(URL url, Map<String, String> additionalMetadata) {
		Objects.requireNonNull(url)
		Metadata metadata = Metadata.builderWithUrl(url)
				.put("profile", profile)
				.putAll(additionalMetadata)
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

		return new Tuple(image, html)
	}
}
