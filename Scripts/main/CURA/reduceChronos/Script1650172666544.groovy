import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.dot.MPGVisualizer
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.Reducer
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
//import java.util.function.BiFunction

/**
 * Test Cases/main/CURA/reduce
 */

assert store != null
assert currentMaterialList != null

WebUI.comment("reduce started; store=${store}")
WebUI.comment("reduce started; currentMaterialList=${currentMaterialList}")

MProductGroup reduced = Reducer.chronos(store, currentMaterialList)

Inspector inspector = Inspector.newInstance(store)
MProductGroup processed = inspector.process(reduced)

if (processed.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(processed.getJobName(), JobTimestamp.now(), processed);
}

return processed
