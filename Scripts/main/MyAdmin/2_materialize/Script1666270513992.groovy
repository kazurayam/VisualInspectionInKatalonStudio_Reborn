import java.nio.file.Files

import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.dot.MPGVisualizer
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.materialize.MaterializingPageFunctions
import com.kazurayam.materialstore.materialize.StorageDirectory
import com.kazurayam.materialstore.materialize.Target
import com.kazurayam.materialstore.materialize.TargetCSVParser
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.util.stream.Collectors

/**
 * Test Cases/main/MyAdmin/materialize
 * 
 */
// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(targetFile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

//List<Target> targetList = TargetCSVReader.parse(targetFile, ["profile":profile])
TargetCSVParser parser = TargetCSVParser.newSimpleParser();
List<Target> targetList = 
	parser.parse(targetFile).stream()
		.map({t -> t.copyWith(["profile": profile])})
		.collect(Collectors.toList())

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebDriver driver = DriverFactory.getWebDriver()
StorageDirectory sd = new StorageDirectory(store, jobName, jobTimestamp)

for (Target target in targetList) {
	WebUI.navigateToUrl(target.getUrl().toExternalForm())	
	
	// take and store the entire screenshot using AShot
	Material screenshot =
		MaterializingPageFunctions.storeEntirePageScreenshot.accept(target, driver, sd)
	assert Files.exists(screenshot.toPath(store))
	
	// take and store the HTML source
	Material html =
		MaterializingPageFunctions.storeHTMLSource.accept(target, driver, sd)
	assert Files.exists(html.toPath(store))
}

WebUI.closeBrowser()
