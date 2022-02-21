import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.resolvent.ArtifactGroup
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.metadata.MetadataPattern
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.MaterialstoreFacade
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
WebUI.comment("Execution Profile ${profile1} was loaded with GlobalVariable.URL=${GlobalVariable.URL}")
JobTimestamp timestampP = JobTimestamp.now()

WebUI.callTestCase(
	findTestCase("main/Flaskr/navigate_through_the_site"),
	["profile": profile1, "store": store, "jobName": jobName, "jobTimestamp": timestampP]
)

// visit the Development environment
String profile2 = "Flaskr_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded with GlobalVariable.URL=${GlobalVariable.URL}")
JobTimestamp timestampD = JobTimestamp.now()

WebUI.callTestCase(
	findTestCase("main/Flaskr/navigate_through_the_site"),
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
	

try {
	MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)
	
	// make ArtifactGroup
	ArtifactGroup prepared = 
    	ArtifactGroup.builder(left, right)
			.ignoreKeys("profile", "URL.host", "URL.port")
			.sort("step")
			.build()
			
	ArtifactGroup workedOut = facade.workOn(prepared)
	
	// difference greater than the criteria should be warned
	double criteria = 0.0d
	
	// compile HTML report
	Path reportFile = facade.reportArtifactGroup(jobName, workedOut, criteria, jobName.toString() + "-index.html")
	assert Files.exists(reportFile)
	WebUI.comment("The report can be found ${reportFile.toString()}")

	int warnings = workedOut.countWarnings(criteria)
	if (warnings > 0) {
		KeywordUtil.markWarning("found ${warnings} differences.")
	}
	
} catch (Exception e) {
	e.printStackTrace()
	KeywordUtil.markWarning(e.getMessage())
}

