import java.nio.file.Path

import com.kazurayam.materialstore.MaterialstoreFacade
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/GoogleSearch/report
 *
 */

assert store != null
assert jobName != null
assert materialList != null

WebUI.comment("report started; materialList=${materialList.toString()}, jobName=${jobName}, store=${store}")


// the file name of HTML report
String fileName = jobName.toString() + "-list.html"

MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)
Path report = facade.report(jobName, materialList, fileName)

return report