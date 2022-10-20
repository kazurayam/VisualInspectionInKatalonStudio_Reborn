import java.nio.file.Path
import java.nio.file.Paths
import java.time.temporal.ChronoUnit

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * Test Cases/main/CURA/cleanup
 * 
 * delete old files in the Materials directory.
 * files aged for 3 days or longer will be deleted
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = Stores.newInstance(root)
JobTimestamp currentTimestamp = JobTimestamp.now()

deleteOldMaterialsOf(store, new JobName("CURA"), currentTimestamp)

/*
 * To save disk space, we will delete the "<JobName>/<JobTimestamp>" dirs
 * in the "Materials" directory. 
 * The files and subdirectories will be deleted recursively.
 * We will choose "<JobTimestamp>" older than 3 days to delete.
 */
def deleteOldMaterialsOf(Store store, JobName jobName, JobTimestamp currentTimestamp) {
	int deletionCount = store.deleteMaterialsOlderThanExclusive(jobName,
		currentTimestamp, 3L, ChronoUnit.DAYS)
	WebUI.comment("deleted ${deletionCount} files out of ${jobName}")
}
