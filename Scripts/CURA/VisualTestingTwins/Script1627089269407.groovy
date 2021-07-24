import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kazurayam.materialstore.store.Store as Store
import com.kazurayam.materialstore.store.StoreImpl as StoreImpl
import com.kazurayam.materialstore.store.JobName as JobName
import com.kazurayam.materialstore.store.JobTimestamp as JobTimestamp
import java.nio.file.Path as Path
import java.nio.file.Paths as Paths
import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Material")
Store store = new StoreImpl(root)
JobName jobName = new JobName("VisualTestingTwins")
JobTimestamp jobTimestamp = JobTimestamp.now()
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

String profileName = "CURA_ProductionEnv"
 
profilesLoader.loadProfile(profileName)
WebUI.comment("Execution Profile ${profileName} was loaded")
WebUI.callTestCase(findTestCase("Test Cases/CURA/visitSite"), ["store": store, "jobName": jobName, "jobTimestamp": jobTimestamp])

//profileName = "CURA_DevelopmentEnv"
//profilesLoader.loadProfile(profileName)
//WebUI.comment("Execution Profile ${profileName} was loaded")
//WebUI.callTestCase(findTestCase("Test Cases/CURA/visitSite"), ["store": store, "jobName": jobName, "jobTimestamp": jobTimestamp])

WebUI.closeBrowser()
