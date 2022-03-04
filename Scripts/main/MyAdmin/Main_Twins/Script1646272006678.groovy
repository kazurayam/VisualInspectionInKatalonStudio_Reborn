import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
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

/**
 * Test Cases/main/MyAdmin/Main_Twins
 *
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("MyAdmin_Main_Twins")

// --------------------------------------------------------------------
/*
 * Materialize stage
 */
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

// visit the Production environment to materialize web resources
String profile1 = "MyAdmin_ProductionEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded")
JobTimestamp timestampP = JobTimestamp.now()
WebUI.callTestCase(
	findTestCase("main/MyAdmin/materialize"),
	["profile": profile1, "store": store, "jobName": jobName, "jobTimestamp": timestampP]
)

// visit the Development environment to materialize web resources
String profile2 = "MyAdmin_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded")
JobTimestamp timestampD = JobTimestamp.laterThan(timestampP)
WebUI.callTestCase(
	findTestCase("main/MyAdmin/materialize"),
	["profile": profile2, "store": store, "jobName": jobName, "jobTimestamp": timestampD]
)
	
// --------------------------------------------------------------------
/*
 * Reduce stage
 */
// identify 2 MaterialList objects: left and right = production and development
// compare the right(development) against the left(project)
// find differences betwee the 2 versions --- Twins mode
MaterialList left = store.select(jobName, timestampP,
			QueryOnMetadata.builder([ "profile": profile1 ]).build()
			)
MaterialList right = store.select(jobName, timestampD,
			QueryOnMetadata.builder([ "profile": profile2 ]).build()
			)

WebUI.comment("left=${left.toString()}")
WebUI.comment("right=${right.toString()}")

MProductGroup reduced =
	WebUI.callTestCase(findTestCase("main/MyAdmin/reduce"),
		["store": store,
			"leftMaterialList": left,
			"rightMaterialList": right
			])


//---------------------------------------------------------------------
/*
 * reporting stage
 */
// compile a human-readable report
int warnings =
	WebUI.callTestCase(findTestCase("main/MyAdmin/report"),
		["store": store, "mProductGroup": reduced, "criteria": 0.0d])



//---------------------------------------------------------------------
/*
 * Epilogue	
 */
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}

	