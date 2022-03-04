import com.kazurayam.materialstore.MaterialstoreFacade
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.util.function.BiFunction

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
	    MProductGroup.builder(left, right)
	        .ignoreKeys("profile", "URL.host", "URL.port")
	        .sort("step")
	        .build()
}

MProductGroup prepared = 
	MProductGroupBuilder.twins(store,
		leftMaterialList, rightMaterialList, func)
	
MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)
MProductGroup reduced = facade.reduce(prepared)

return reduced


