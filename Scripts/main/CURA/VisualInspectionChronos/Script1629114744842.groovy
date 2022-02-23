import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.materialstore.MaterialstoreFacade
import com.kazurayam.materialstore.MaterialstoreFacade.Result
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.resolvent.ArtifactGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * CURA/VisualInspectionChronos
 * 
 */
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

Metadata metadata = WebUI.callTestCase(
	findTestCase("main/CURA/visitCURA"),
	[ "store": store, "jobName": jobName, "jobTimestamp": currentTimestamp ]
)

// identify the last jobTimestamp that were created previously
QueryOnMetadata query = QueryOnMetadata.builderWithMetadata(metadata).build()
JobTimestamp previousTimestamp = store.queryJobTimestampPriorTo(jobName, query, currentTimestamp)

if (previousTimestamp == JobTimestamp.NULL_OBJECT) {
	KeywordUtil.markFailedAndStop("previous JobTimestamp prior to ${currentTimestamp} is not found")
}

// Look up the materials stored in the previous time of run
MaterialList left = store.select(jobName, previousTimestamp, QueryOnMetadata.ANY)
assert left.size() > 0

// Look up the materials stored in the current time of run
MaterialList right = store.select(jobName, currentTimestamp, QueryOnMetadata.ANY)
assert right.size() > 0

// the facade class that work for you
MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)

// do comparing while creating diff. The result will be carried as instances of Artifact class.
ArtifactGroup prepared =
    ArtifactGroup.builder(left, right)
		.ignoreKeys("URL", "URL.host")
		.build()

// if difference is greater than this criteria value, it should be warned
double criteria = 0.1d

// the file name of HTML report
String fileName = jobName.toString() + "-index.html"

// make diff and compile report
Result result = facade.makeDiffAndReport(jobName, prepared, criteria, fileName)

assert Files.exists(result.report())
WebUI.comment("The report can be found at ${result.report()}")

// if any significant difference found, this Test Case should warn it
if (result.warnings() > 0) {
	KeywordUtil.markWarning("found ${result.warnings()} differences")
}
