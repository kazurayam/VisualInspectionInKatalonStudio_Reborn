import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.base.manage.StoreCleaner
import com.kazurayam.materialstore.core.filesystem.JobName
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.core.filesystem.Store
import com.kazurayam.materialstore.core.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/main/common/Scavenge
 * 
 * cleans up old artifacts of all JobNames in the remote store.
 * 
 * we will retain artifacts within 5 days; clean up the older than 5 days.
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path remote = projectDir.resolve("store-backup")
Store backup = Stores.newInstance(remote)
StoreCleaner cleaner = StoreCleaner.newInstance(backup)

JobTimestamp olderThan = JobTimestamp.now().minusDays(5)

List<JobName> jobNames = backup.findAllJobNames()
for (JobName jobName : jobNames) {
	cleaner.cleanup(jobName, olderThan)
}
