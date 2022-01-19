import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.DiffArtifacts
import com.kazurayam.materialstore.JobName
import com.kazurayam.materialstore.JobTimestamp
import com.kazurayam.materialstore.MaterialList
import com.kazurayam.materialstore.IgnoringMetadataKeys
import com.kazurayam.materialstore.MetadataPattern
import com.kazurayam.materialstore.Store
import com.kazurayam.materialstore.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("Flaskr_VisualInspectionTwins")
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

// --------------------------------------------------------------------

// visit the Production environment
String profile1 = "Flaskr_ProductionEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded")
JobTimestamp timestampP = JobTimestamp.now()
WebUI.callTestCase(
	findTestCase("main/Flaskr/visitFlaskr"),
	["profile": profile1, "store": store, "jobName": jobName, "jobTimestamp": timestampP]
)

// visit the Development environment
String profile2 = "Flaskr_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded")
JobTimestamp timestampD = JobTimestamp.now()
WebUI.callTestCase(
	findTestCase("main/Flaskr/visitFlaskr"),
	["profile": profile2, "store": store, "jobName": jobName, "jobTimestamp": timestampD]
)

// --------------------------------------------------------------------

// compare the materials obtained from the 2 sites, compile a diff report

// pickup the materials that belongs to the 2 "profiles"
MaterialList left = store.select(jobName, timestampP,
	MetadataPattern.builderWithMap([ "profile": profile1 ]).build()
	)

MaterialList right = store.select(jobName, timestampD,
	MetadataPattern.builderWithMap([ "profile": profile2 ]).build()
	)
	
// difference greater than the criteria should be warned
double criteria = 0.0d

// make DiffArtifacts
DiffArtifacts stuffedDiffArtifacts = 
    store.makeDiff(left, right, IgnoringMetadataKeys.of("profile", "URL.host"))
int warnings = stuffedDiffArtifacts.countWarnings(criteria)

// compile HTML report
Path reportFile = 
    store.reportDiffs(jobName, stuffedDiffArtifacts, criteria,
	    jobName.toString() + "-index.html")
assert Files.exists(reportFile)
WebUI.comment("The report can be found ${reportFile.toString()}")

if (warnings > 0) {
	KeywordUtil.markFailed("found ${warnings} differences.")
}
