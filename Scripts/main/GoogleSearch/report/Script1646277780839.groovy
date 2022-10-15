import java.nio.file.Path

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.SortKeys
import com.kazurayam.materialstore.inspector.Inspector
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/GoogleSearch/report
 *
 */

assert store != null
assert materialList != null

JobName jobName = materialList.getJobName()

WebUI.comment("report started; materialList=${materialList.toString()}, jobName=${jobName}, store=${store}")


// the file name of HTML report
String fileName = jobName.toString() + "-list.html"

Inspector inspector = Inspector.newInstance(store)
SortKeys sortKeys = new SortKeys("step","URL.host", "URL.path")
inspector.setSortKeys(sortKeys)
Path report = inspector.report(materialList, fileName)

return report