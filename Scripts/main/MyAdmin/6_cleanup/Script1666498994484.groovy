import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.manage.StoreCleaner

/*
 * MyAdmin/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

StoreCleaner cleaner = StoreCleaner.newInstance(store)
cleaner.cleanup(jobName)
