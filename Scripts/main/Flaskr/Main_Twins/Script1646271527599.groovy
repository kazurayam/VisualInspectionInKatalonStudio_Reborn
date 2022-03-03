import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.MaterialstoreFacade
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Flaskr/VisualInspection_Twins
 *
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("Flaskr_Main_Twins")

ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()


//---------------------------------------------------------------------
/*
 * Materialize stage
 */
// visit the Production environment
String profile1 = "Flaskr_ProductionEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded with GlobalVariable.URL=${GlobalVariable.URL}")
JobTimestamp timestampP = JobTimestamp.now()

WebUI.callTestCase(
	findTestCase("main/Flaskr/materialize"),
	["profile": profile1, "store": store, "jobName": jobName, "jobTimestamp": timestampP]
)

// visit the Development environment
String profile2 = "Flaskr_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded with GlobalVariable.URL=${GlobalVariable.URL}")
JobTimestamp timestampD = JobTimestamp.laterThan(timestampP)

WebUI.callTestCase(
	findTestCase("main/Flaskr/materialize"),
	["profile": profile2, "store": store, "jobName": jobName, "jobTimestamp": timestampD]
)



// --------------------------------------------------------------------
/*
 * Reduce stage
 */
// identify 2 MaterialList objects: left and right = production and development
// compare the right(development) with the left(production)
// in order to find differences between the 2 versions. --- Twins mode

MaterialList left = store.select(jobName, timestampP,
	QueryOnMetadata.builder([ "profile": profile1 ]).build())

MaterialList right = store.select(jobName, timestampD,
	QueryOnMetadata.builder([ "profile": profile2 ]).build())

WebUI.comment("left=${left.toString()}")
WebUI.comment("right=${right.toString()}")

MProductGroup reduced =
    WebUI.callTestCase(findTestCase("main/Flaskr/reduce"),
		["store": store, "jobName": jobName,
			"leftMaterialList": left,
			"rightMaterialList": right])


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
