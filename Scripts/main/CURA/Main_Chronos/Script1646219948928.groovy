import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * CURA/VisualInspection_Chronos
 *
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("CURA_VisualInspectionChronos")

JobTimestamp currentTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

//---------------------------------------------------------------------
/*
 * Materialize stage
 */
String profile = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile)
WebUI.comment("Execution Profile ${profile} was loaded")

// visit "http://demoaut-mimic.katalon.com", take screenshots and page source
 WebUI.callTestCase(
	findTestCase("main/CURA/materialize"),
		[ "store": store, "jobName": jobName, "jobTimestamp": currentTimestamp ])




//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a previous jobTimesamp directory.
// compare the current materials with the previos one
// in order to find differences between the 2 versions. --- Chronos mode

JobTimestamp previousTimestamp =
	store.queryJobTimestampWithSimilarContentPriorTo(jobName, currentTimestamp)

WebUI.comment("previousTimestamp=${previousTimestamp.toString()}")
WebUI.comment("currentTimestamp=${currentTimestamp.toString()}")


MProductGroup reduced =
	WebUI.callTestCase(findTestCase("main/CURA/reduce"),
		["store": store, "jobName": jobName,
			"previousTimestamp": previousTimestamp,
			"currentTimestamp": currentTimestamp
			])


//---------------------------------------------------------------------
/*
 * Report stage
 */
// compile a human-readable report
int warnings =
	WebUI.callTestCase(findTestCase("main/CURA/report"),
		["store": store, "jobName": jobName, "mProductGroup": reduced, "criteria": 0.0d])


//---------------------------------------------------------------------
/*
 * Epilogue
 */
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}