import com.kazurayam.materialstore.filesystem.JobTimestamp

/*
 * CURA/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

JobTimestamp oldestJobTimestampToRetain = store.findNthJobTimestamp(jobName, 2)
return store.deleteStuffOlderThanExclusive(jobName, oldestJobTimestampToRetain)
