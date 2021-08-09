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
import com.kazurayam.materialstore.StoreImpl
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = new StoreImpl(root)
JobName jobName = new JobName(GlobalVariable.CURRENT_TESTCASE_NAME)
JobTimestamp currentTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

//-----------------------------------------------------------------------------
String profile = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile)
WebUI.comment("Execution Profile ${profile} was loaded")

// visit "http://demoaut-mimic.katalon.com", take screenshots and page source 

WebUI.callTestCase(
	findTestCase("CURA/visitCURA"),
	[ "store": store, "jobName": jobName, "jobTimestamp": currentTimestamp ]
)

	
// compare the latest materials with those taken in the previous run, produce a diff report

// identify the last jobTimestamp that were created previously
JobTimestamp lastTimestamp = store.findJobTimestampPriorTo(jobName, currentTimestamp)

if (lastTimestamp == JobTimestamp.NULL_OBJECT) {
	KeywordUtil.markFailedAndStop("previous JobTimestamp prior to ${currentTimestamp} is not found")
}
		
List<Material> left = store.select(jobName, lastTimestamp, MetadataPattern.ANY)
assert left.size() > 0

List<Material> right = store.select(jobName, currentTimestamp, MetadataPattern.ANY)
assert right.size() > 0

// difference greater than the criteria should be warned
double criteria = 0.1d

// make DiffArtifacts
DiffArtifacts stuffedDiffArtifacts =
    store.makeDiff(left, right,
		new MetadataIgnoredKeys.Builder()
		    .ignoreKey("URL")
			.ignoreKey("URL.host")
		    .build())
int warnings = stuffedDiffArtifacts.countWarnings(criteria)

// compile HTML report
Path reportFile = store.reportDiffs(jobName, stuffedDiffArtifacts, criteria, jobName.toString() + "-index.html")
assert Files.exists(reportFile)
WebUI.comment("The report can be found at ${reportFile.toString()}")

if (warnings > 0) {
	KeywordUtil.markFailedAndStop("found ${warnings} differences.")
}
