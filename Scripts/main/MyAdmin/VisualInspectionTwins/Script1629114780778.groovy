import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.DiffArtifacts
import com.kazurayam.materialstore.JobName
import com.kazurayam.materialstore.JobTimestamp
import com.kazurayam.materialstore.Material
import com.kazurayam.materialstore.MetadataIgnoredKeys
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
JobName jobName = new JobName("MyAdmin_VisualInspectionTwins")
JobTimestamp jobTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

// --------------------------------------------------------------------

// visit the Production environment
String profile1 = "MyAdmin_ProductionEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded")

WebUI.callTestCase(
	findTestCase("main/MyAdmin/visitMyAdminTopPage"),
	["profile": profile1, "store": store, "jobName": jobName, "jobTimestamp": jobTimestamp]
)

	
// visit the Development environment
String profile2 = "MyAdmin_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded")

WebUI.callTestCase(
	findTestCase("main/MyAdmin/visitMyAdminTopPage"),
	["profile": profile2, "store": store, "jobName": jobName, "jobTimestamp": jobTimestamp]
)
	
// --------------------------------------------------------------------

// compare the materials obtained from the 2 sites, compile a diff report

// pickup the materials that belongs to the 2 "profiles"
List<Material> left = store.select(jobName, jobTimestamp,
			MetadataPattern.builderWithMap([ "profile": profile1 ]).build()
			)

List<Material> right = store.select(jobName, jobTimestamp,
			MetadataPattern.builderWithMap([ "profile": profile2 ]).build()
			)

// difference greater than the criteria should be warned
double criteria = 0.0d

// make DiffArtifacts
DiffArtifacts stuffedDiffArtifacts =
	store.makeDiff(left, right, 
		MetadataIgnoredKeys.of("profile", "URL.host"))
int warnings = stuffedDiffArtifacts.countWarnings(criteria)

// compile HTML report
Path reportFile = store.reportDiffs(jobName, stuffedDiffArtifacts, criteria, jobName.toString() + "-index.html")
assert Files.exists(reportFile)
WebUI.comment("The report can be found ${reportFile.toString()}")

if (warnings > 0) {
	KeywordUtil.markFailed("found ${warnings} differences.")
}

	