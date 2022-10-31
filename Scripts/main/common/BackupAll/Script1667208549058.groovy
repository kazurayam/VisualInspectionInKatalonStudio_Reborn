
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.base.manage.StoreExport
import com.kazurayam.materialstore.core.filesystem.JobName
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.core.filesystem.Store
import com.kazurayam.materialstore.core.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/main/common/BackupAll
 *
 * copy all artifacts from the current local store into the remote store.
 *
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Path remote = projectDir.resolve("store-backup")
Store store = Stores.newInstance(local)
Store backup = Stores.newInstance(remote)

StoreExport export = StoreExport.newInstance(store, backup)

JobTimestamp newerThanOrEqualTo = new JobTimestamp("19700101_000000")

List<JobName> jobNames = backup.findAllJobNames()

for (JobName jobName : jobNames) {
	export.exportReports(jobName, newerThanOrEqualTo)
}
