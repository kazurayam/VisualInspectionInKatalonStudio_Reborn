import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.base.manage.StoreCleaner

/*
 * MyAdmin/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

StoreCleaner cleaner = StoreCleaner.newInstance(store)
cleaner.cleanup(jobName)
