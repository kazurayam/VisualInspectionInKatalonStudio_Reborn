import com.kazurayam.materialstore.filesystem.JobTimestamp

/*
 * GoogleSearch/6_cleanup
 */
Objects.requireNonNull(store);
Objects.requireNonNull(jobName);

JobTimestamp oldestJobTimestampToRetain = store.findNthJobTimestamp(jobName, 1)
return store.deleteStuffOlderThanExclusive(jobName, oldestJobTimestampToRetain)
