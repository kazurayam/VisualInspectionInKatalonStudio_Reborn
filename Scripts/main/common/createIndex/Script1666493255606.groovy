import com.kazurayam.materialstore.report.IndexCreator

Objects.requireNonNull(store)

IndexCreator indexCreator = new IndexCreator(store);
return indexCreator.create(); 
