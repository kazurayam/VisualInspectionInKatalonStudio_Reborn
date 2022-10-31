import com.kazurayam.materialstore.base.report.IndexCreator

Objects.requireNonNull(store)

IndexCreator indexCreator = new IndexCreator(store);
return indexCreator.create(); 
