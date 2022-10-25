import com.kazurayam.materialstore.manage.StoreCleaner

/*
 * CURA/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

StoreCleaner cleaner = StoreCleaner.newInstance(store)
cleaner.cleanup(jobName)
