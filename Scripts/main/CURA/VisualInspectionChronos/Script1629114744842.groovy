import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.diffartifact.DiffArtifactGroup
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
JobName jobName = new JobName("CURA_VisualInspectionChronos")
JobTimestamp currentTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

//-----------------------------------------------------------------------------
String profile = "CURA_DevelopmentEnv"
profilesLoader.loadProfile(profile)
WebUI.comment("Execution Profile ${profile} was loaded")

// visit "http://demoaut-mimic.katalon.com", take screenshots and page source 

WebUI.callTestCase(
	findTestCase("main/CURA/visitCURA"),
	[ "store": store, "jobName": jobName, "jobTimestamp": currentTimestamp ]
)

// identify the last jobTimestamp that were created previously
JobTimestamp previousTimestamp = store.findJobTimestampPriorTo(jobName, currentTimestamp)

if (previousTimestamp == JobTimestamp.NULL_OBJECT) {
	KeywordUtil.markFailedAndStop("previous JobTimestamp prior to ${currentTimestamp} is not found")
}

// Look up the materials stored in a previous time of run
MaterialList left = store.select(jobName, previousTimestamp, MetadataPattern.ANY)
assert left.size() > 0

// Look up the materials stored in the current time of run
MaterialList right = store.select(jobName, currentTimestamp, MetadataPattern.ANY)
assert right.size() > 0

// if difference is greater than this criteria value, the difference should be marked
double criteria = 0.1d

// do comaring while creating diff. The result will be carried as instances of DiffArtifact class.
DiffArtifactGroup prepared =
    DiffArtifactGroup.builder(left, right)
		.ignoreKeys("URL", "URL.host")
		.build()
MaterialstoreFacade facade = new MaterialstoreFacade(store)
DiffArtifactGroup workedOut = facade.workOn(prepared)

// How many siginificant differences were found?
int warnings = workedOut.countWarnings(criteria)

// compile HTML report
Path reportFile = store.reportDiffs(jobName, workedOut, criteria, jobName.toString() + "-index.html")
assert Files.exists(reportFile)
WebUI.comment("The report can be found at ${reportFile.toString()}")

// if any siginificant difference found, this Test Case should FAIL
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}
