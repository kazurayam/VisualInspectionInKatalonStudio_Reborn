import java.nio.file.Path

import com.kazurayam.materialstore.core.filesystem.SortKeys
import com.kazurayam.materialstore.base.inspector.Inspector

/**
 * Test Cases/main/GoogleSearch/report
 *
 */

assert store != null
assert materialList != null

Inspector inspector = Inspector.newInstance(store)
SortKeys sortKeys = new SortKeys("step","URL.host", "URL.path")
inspector.setSortKeys(sortKeys)
Path report = inspector.report(materialList)

return report