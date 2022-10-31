import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.base.manage.StoreCleaner

/*
 * DuckDuckGo/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

StoreCleaner cleaner = StoreCleaner.newInstance(store)
cleaner.cleanup(jobName, JobTimestamp.now().minusHours(3))
// will cleanup the results while retaining ones in the last 2 hours
