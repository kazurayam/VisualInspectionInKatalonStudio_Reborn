import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.store.DiffArtifacts
import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Material
import com.kazurayam.materialstore.store.MetadataIgnoredKeys
import com.kazurayam.materialstore.store.MetadataPattern
import com.kazurayam.materialstore.store.Store
import com.kazurayam.materialstore.store.StoreImpl
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = new StoreImpl(root)
JobName jobName = new JobName("VisualTestingChronos")
JobTimestamp currentTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

//-----------------------------------------------------------------------------

// visit the Development environment, store page screenshots and HTML sources into the Store 
String profile1 = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile1)
WebUI.comment("Execution Profile ${profile1} was loaded")
WebUI.callTestCase(
	findTestCase("Test Cases/CURA/visitSite"),
	["profile": profile1, "store": store, "jobName": jobName,
		"jobTimestamp": currentTimestamp]
	)

//-----------------------------------------------------------------------------
	
// compare the previous materials and the latest materials, produce a diff report

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
Path reportFile = store.reportDiffs(jobName, stuffedDiffArtifacts, criteria, "index-chronos.html")
assert Files.exists(reportFile)
WebUI.comment("The report can be found at ${reportFile.toString()}")

if (warnings > 0) {
	KeywordUtil.markFailedAndStop("found ${warnings} differences.")
}
