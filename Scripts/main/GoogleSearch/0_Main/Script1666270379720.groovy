import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.core.filesystem.JobName
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.core.filesystem.MaterialList
import com.kazurayam.materialstore.core.filesystem.Store
import com.kazurayam.materialstore.core.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * Test Cases/main/GoogleSearch/Main
 * 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Path remote = projectDir.resolve("store-backup")
Store store = Stores.newInstance(local)
Store backup = Stores.newInstance(remote)
JobName jobName = new JobName("GoogleSearch")
JobTimestamp jobTimestamp = JobTimestamp.now()


//---------------------------------------------------------------------
/*
 * restorePrevious
 */
WebUI.callTestCase(findTestCase("main/GoogleSearch/1_restorePrevious"),
		["backup": backup, "store": store, "jobName": jobName])


//---------------------------------------------------------------------
/*
 * materialize stage
 */
MaterialList materialList = WebUI.callTestCase(
	findTestCase("main/GoogleSearch/2_materialize"),
	["store": store, "jobName": jobName, "jobTimestamp": jobTimestamp])



//---------------------------------------------------------------------
/*
 * report stage
 */
Path report = 
	WebUI.callTestCase(findTestCase("main/GoogleSearch/4_report"),
		["store": store, "materialList": materialList])


//---------------------------------------------------------------------
/*
 * backupLatest
 */
WebUI.callTestCase(findTestCase("main/GoogleSearch/5_backupLatest"),
		["store": store, "backup": backup, "jobName": jobName])
	

//---------------------------------------------------------------------
/*
 * cleanup
 */
WebUI.callTestCase(findTestCase("main/GoogleSearch/6_cleanup"),
		["store": store, "jobName": jobName])
	
	
//---------------------------------------------------------------------
/*
 * create store/index.html
 */
Path index =
	WebUI.callTestCase(findTestCase("main/GoogleSearch/7_index"),
		["store": store])
	
		
		
//---------------------------------------------------------------------	
/*
 * Epilogue
 */
WebUI.comment("see ${report.toString()} for the list")
