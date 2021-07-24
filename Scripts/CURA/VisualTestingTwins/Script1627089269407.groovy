import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.store.DiffArtifact
import com.kazurayam.materialstore.store.DiffReporter
import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Material
import com.kazurayam.materialstore.store.MetadataPattern
import com.kazurayam.materialstore.store.Store
import com.kazurayam.materialstore.store.StoreImpl
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil


Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = new StoreImpl(root)
JobName jobName = new JobName("VisualTestingTwins")
JobTimestamp jobTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

// --------------------------------------------------------------------

// visit the Production site
String profile1 = "CURA_ProductionEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded")
WebUI.callTestCase(
	findTestCase("Test Cases/CURA/visitSite"),
	["profile": profile1, "store": store, "jobName": jobName, 
		"jobTimestamp": jobTimestamp]
	)

	
// visit the Development site
String profile2 = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile2)
WebUI.comment("Execution Profile ${profile2} was loaded")
WebUI.callTestCase(
	findTestCase("Test Cases/CURA/visitSite"),
	["profile": profile2, "store": store, "jobName": jobName,
		"jobTimestamp": jobTimestamp]
)
	
// --------------------------------------------------------------------

// compare the Materials of the 2 sites, produce a report

	
// pickup the materials that belongs to the 2 "profiles"
List<Material> expected = store.select(jobName, jobTimestamp,
			new MetadataPattern([ "profile": profile1 ]))

List<Material> actual = store.select(jobName, jobTimestamp,
			new MetadataPattern([ "profile": profile2 ]))

// make diff
List<DiffArtifact> stuffedDiffArtifacts =
	store.makeDiff(expected, actual, ["URL.file"] as Set)

// compile HTML report
DiffReporter reporter = store.newReporter(jobName)
int warnings = reporter.reportDiffs(stuffedDiffArtifacts, "index.html")

Path reportFile = root.resolve("index.html")
assert Files.exists(reportFile)

if (warnings > 0) {
	KeywordUtil.markFailedAndStop("found ${warnings} differences.")
}
	