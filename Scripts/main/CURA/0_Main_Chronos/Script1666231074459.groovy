import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.core.filesystem.JobName
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.core.filesystem.MaterialList
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.core.filesystem.SortKeys
import com.kazurayam.materialstore.core.filesystem.Store
import com.kazurayam.materialstore.core.filesystem.Stores
import com.kazurayam.materialstore.base.reduce.MaterialProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/CURA/Main_Chronos
 *
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Path remote = projectDir.resolve("store-backup")
Store store = Stores.newInstance(local)
Store backup = Stores.newInstance(remote)
JobName jobName = new JobName("CURA")


//---------------------------------------------------------------------
/*
 * restorePrevious
 */
WebUI.callTestCase(findTestCase("main/CURA/1_restorePrevious"),
		["backup": backup, "store": store, "jobName": jobName])


//---------------------------------------------------------------------
/*
 * Materialize stage
 */
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

// visit the site
String profile = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile)
WebUI.comment("Execution Profile ${profile} was loaded")

// visit "http://demoaut-mimic.katalon.com", take screenshots and page source
JobTimestamp currentTimestamp = JobTimestamp.now()

 WebUI.callTestCase(
	findTestCase("main/CURA/2_materialize"),
		[ "store": store, "jobName": jobName, "jobTimestamp": currentTimestamp ])




//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a MaterialList which is created by the current run of Materialize stage
// call "reduce" which will 
// - identify the MaterialList which was created by the previous run of Materialize stage
// - compare 2 Materialists, create a MProductGroup and return

MaterialList currentMaterialList = store.select(jobName, currentTimestamp, QueryOnMetadata.ANY)

SortKeys sortKeys = new SortKeys("step", "profile", "URL.host", "URL.path", "URL.fragment")

MaterialProductGroup reduced =
	WebUI.callTestCase(findTestCase("main/CURA/3_reduceChronos"),
		["store": store, "currentMaterialList": currentMaterialList, "sortKeys": sortKeys])


if (reduced != null) {
	//---------------------------------------------------------------------
	/*
	 * Report stage
	 */
	// compile a human-readable report
	int warnings =
		WebUI.callTestCase(findTestCase("main/CURA/4_report"),
			["store": store, "mProductGroup": reduced, "sortKeys": sortKeys, "criteria": 1.0d])

			
	//---------------------------------------------------------------------
	/*
	 * Epilogue
	 */
	if (warnings > 0) {
		KeywordUtil.markWarning("found ${warnings} differences.")
	}
	
	//---------------------------------------------------------------------
	/*
	 * backupLatest
	 */
	WebUI.callTestCase(findTestCase("main/CURA/5_backupLatest"),
			["store": store, "backup": backup, "jobName": jobName])
	
	//-------------------------------------------------------------------------
	/*
	 * clean up the old stuff of the CURA job in the store
	 */
	WebUI.callTestCase(findTestCase("main/CURA/6_cleanup"),
			["store": store, "jobName": jobName])
	
	//---------------------------------------------------------------------
	/*
	 * create store/index.html
	 */
	Path index =
		WebUI.callTestCase(findTestCase("main/CURA/7_index"),
			["store": store])
	
}