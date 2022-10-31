import com.kazurayam.materialstore.base.inspector.Inspector
import com.kazurayam.materialstore.diagram.dot.MPGVisualizer
import com.kazurayam.materialstore.core.filesystem.MaterialList
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.core.filesystem.SortKeys
import com.kazurayam.materialstore.base.reduce.MaterialProductGroup
import com.kazurayam.materialstore.base.reduce.Reducer
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
//import java.util.function.BiFunction

/**
 * Test Cases/main/CURA/3_reduceChronos
 */

assert store != null
assert currentMaterialList != null
assert sortKeys != null

WebUI.comment("store=${store}")
WebUI.comment("currentMaterialList=${currentMaterialList}")
WebUI.comment("sortKeys=${sortKeys}")

MaterialProductGroup reduced = Reducer.chronos(store, currentMaterialList)

Inspector inspector = Inspector.newInstance(store)
inspector.setSortKeys(sortKeys)
MaterialProductGroup inspected = inspector.reduceAndSort(reduced)

if (inspected.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(inspected.getJobName(), JobTimestamp.now(), inspected);
}

return inspected