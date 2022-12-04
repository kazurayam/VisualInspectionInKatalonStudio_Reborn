import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.base.report.IndexCreator
import com.kazurayam.materialstore.core.filesystem.Store
import com.kazurayam.materialstore.core.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Store store = Stores.newInstance(local)

IndexCreator indexCreator = new IndexCreator(store);
return indexCreator.create(); 
