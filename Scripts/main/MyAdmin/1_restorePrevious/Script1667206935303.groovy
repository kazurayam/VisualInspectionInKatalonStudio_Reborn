import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.base.manage.StoreImport

/*
 * MyAdmin/1_restorePrevious
 */
Objects.requireNonNull(store);
Objects.requireNonNull(backup);
Objects.requireNonNull(jobName);

StoreImport importer = StoreImport.newInstance(backup, store)
importer.importReports(jobName);

