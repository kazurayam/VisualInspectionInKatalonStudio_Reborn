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

MProductGroup reduced = 
        MProductGroup.builder(leftMaterialList, rightMaterialList)
		    .ignoreKeys("profile", "URL.host", "URL.port")
			.sort("step")
			.build()	
Inspector inspector = Inspector.newInstance(store)
MProductGroup processed = inspector.process(reduced)

if (processed.getNumberOfBachelors() > 0) {
	// if any bachelor found, generate diagram of MProductGroup object
	MPGVisualizer visualizer = new MPGVisualizer(store)
	visualizer.visualize(processed.getJobName(), JobTimestamp.now(), reduced);
}

return processed
