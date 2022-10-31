import com.kazurayam.materialstore.base.inspector.Inspector
import com.kazurayam.materialstore.base.reduce.MaterialProductGroup
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.diagram.dot.MPGVisualizer
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/MyAdmin/reduce
 *
 */
assert store != null
assert leftMaterialList != null
assert rightMaterialList != null
assert sortKeys != null

WebUI.comment("reduce started; store=${store}")
WebUI.comment("reduce started; leftMaterialList=${leftMaterialList}")
WebUI.comment("reduce started; rightMaterialList=${rightMaterialList}")

assert leftMaterialList.size() > 0
assert rightMaterialList.size() > 0

MaterialProductGroup reduced =
		MaterialProductGroup.builder(leftMaterialList, rightMaterialList)
			.ignoreKeys("profile", "URL.host", "URL.port")
			.sort("step")
			.build()
Inspector inspector = Inspector.newInstance(store)
inspector.setSortKeys(sortKeys)
MaterialProductGroup inspected = inspector.reduceAndSort(reduced)

if (inspected.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(inspected.getJobName(), JobTimestamp.now(), reduced);
}

return inspected
