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
//WebUI.comment("reduce started; leftMaterialList=${leftMaterialList}")
//WebUI.comment("reduce started; rightMaterialList=${rightMaterialList}")

assert leftMaterialList.size() > 0
assert rightMaterialList.size() > 0

MaterialProductGroup mpg =
		MaterialProductGroup.builder(leftMaterialList, rightMaterialList)
			.ignoreKeys("profile", "URL.host", "URL.port")
			.labelLeft("ProductionEnv")
			.labelRight("DevelopmentEnv")
			.sort("step")
			.build()

WebUI.comment("mpg.getCountTotal()=" + mpg.getCountTotal())

Inspector inspector = Inspector.newInstance(store)
inspector.setSortKeys(sortKeys)
MaterialProductGroup reduced = inspector.reduceAndSort(mpg)

WebUI.comment("inspected.getCountTotal()=" + reduced.getCountTotal())

if (reduced.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(reduced.getJobName(), JobTimestamp.now(), reduced);
}

return reduced
