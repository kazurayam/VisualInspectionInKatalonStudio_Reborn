import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
//import java.util.function.BiFunction

/**
 * Test Cases/main/CURA/reduce
 */

assert store != null
assert currentMaterialList != null

WebUI.comment("reduce started; store=${store}")
WebUI.comment("reduce started; currentMaterialList=${currentMaterialList}")

MProductGroup prepared = MProductGroupBuilder.chronos(store, currentMaterialList)

Inspector inspector = Inspector.newInstance(store)
MProductGroup reduced = inspector.reduce(prepared)

return reduced

/*
 BiFunction<MaterialList, MaterialList, MProductGroup> func = {
	 MaterialList left, MaterialList right ->
		 MProductGroup.builder(left, right)
			 .build()
 }
 MProductGroup reduced = MProductGroupBuilder.chronos(store, currentMaterialList, func)
 return reduced
  */
 