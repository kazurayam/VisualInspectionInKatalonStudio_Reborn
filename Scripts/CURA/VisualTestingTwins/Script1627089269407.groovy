import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Store
import com.kazurayam.materialstore.store.StoreImpl
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = new StoreImpl(root)
JobName jobName = new JobName("VisualTestingTwins")
JobTimestamp jobTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()
String profileName = ''

// --------------------------------------------------------------------

// visit the Production site
profileName = "CURA_ProductionEnv"
profilesLoader.loadProfile(profileName)
WebUI.comment("Execution Profile ${profileName} was loaded")
WebUI.callTestCase(
	findTestCase("Test Cases/CURA/visitSite"),
	["profile": profileName, "store": store, "jobName": jobName, 
		"jobTimestamp": jobTimestamp]
	)

	
// visit the Development site
profileName = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profileName)
WebUI.comment("Execution Profile ${profileName} was loaded")
WebUI.callTestCase(
	findTestCase("Test Cases/CURA/visitSite"),
	["profile": profileName, "store": store, "jobName": jobName,
		"jobTimestamp": jobTimestamp]
	)
	
// --------------------------------------------------------------------