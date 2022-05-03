import com.kazurayam.materialstore.dot.MPGVisualizer
import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.Reducer
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.util.function.BiFunction

/**
 * Test Cases/main/MyAdmin/reduce
 * 
 */
assert store != null
assert leftMaterialList != null
assert rightMaterialList != null

WebUI.comment("reduce started; store=${store}")
WebUI.comment("reduce started; leftMaterialList=${leftMaterialList}")
WebUI.comment("reduce started; rightMaterialList=${rightMaterialList}")

assert leftMaterialList.size() > 0
assert rightMaterialList.size() > 0

BiFunction<MaterialList, MaterialList, MProductGroup> func = {
	MaterialList left, MaterialList right ->
		MProductGroup.builder(leftMaterialList, rightMaterialList)
			.ignoreKeys("profile", "URL.host", "URL.port")
			.sort("step")
			.build()
}

MProductGroup prepared =
	Reducer.twins(store,
		leftMaterialList, rightMaterialList, func)
	
Inspector inspector = Inspector.newInstance(store)
MProductGroup reduced = inspector.reduce(prepared)

if (reduced.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(reduced.getJobName(), JobTimestamp.now(), reduced);
}

return reduced
