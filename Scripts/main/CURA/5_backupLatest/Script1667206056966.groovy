import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.base.manage.StoreExport

/*
 * CURA/5_backupLatest
 */
Objects.requireNonNull(store);
Objects.requireNonNull(backup);
Objects.requireNonNull(jobName);

StoreExport export = StoreExport.newInstance(store, backup)
export.exportReports(jobName);

