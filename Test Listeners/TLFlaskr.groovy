import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.subprocessj.docker.ContainerFinder;
import com.kazurayam.subprocessj.docker.ContainerRunner;
import com.kazurayam.subprocessj.docker.ContainerRunner.Builder;
import com.kazurayam.subprocessj.docker.ContainerStopper;
import com.kazurayam.subprocessj.docker.ContainerFinder.ContainerFindingResult;
import com.kazurayam.subprocessj.docker.ContainerRunner.ContainerRunningResult;
import com.kazurayam.subprocessj.docker.ContainerStopper.ContainerStoppingResult;
import com.kazurayam.subprocessj.docker.model.ContainerId;
import com.kazurayam.subprocessj.docker.model.DockerImage;
import com.kazurayam.subprocessj.docker.model.NameValuePair;
import com.kazurayam.subprocessj.docker.model.PublishedPort;

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.util.KeywordUtil


class TLFlaskr {

	private final PublishedPort FLASKR_PROD_PORT = new PublishedPort(3080, 8080)
	private final PublishedPort FLASKR_DEV_PORT  = new PublishedPort(3090, 8080)
	private final DockerImage image = new DockerImage("kazurayam/flaskr-kazurayam:1.1.0")
	
	/**
	 * start the Flaskr servers at http://127.0.0.1:3080/ and http://127.0.0.1:3090/
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (testCaseContext.getTestCaseId().contains("Flaskr/VisualInspection_Twins")) {
			Path projectDir = Paths.get(RunConfiguration.getProjectDir())
			Path tmpDir = projectDir.resolve("tmp")
			Files.createDirectories(tmpDir)
			
			// start the Production system
			startupFlaskr(tmpDir, "TL_Flaskr_prod", FLASKR_PROD_PORT, image)
			
			// start the Development system
			List<NameValuePair> envVars = new ArrayList<>();
			envVars.add(new NameValuePair("FLASKR_ALT_VIEW", "true"))
			startupFlaskr(tmpDir, "TL_Flaskr_dev", envVars, FLASKR_DEV_PORT, image)
			
			// wait for a while so that the servers get ready
			Thread.sleep(3000)
		}
	}
	
	/*
	 * 
	 */
	void startupFlaskr(Path baseDir, String dirName,
		PublishedPort port, DockerImage image)
	{
		startupFlaskr(baseDir, dirName, new ArrayList<NameValuePair>(), port, image)
	}

	/*
	 * startup Flasker server
	 */
	void startupFlaskr(Path baseDir, String dirName, 
			List<NameValuePair> envVars, PublishedPort port, DockerImage image)
	{
		Path webDir = baseDir.resolve(dirName)
		Files.createDirectories(webDir)
		ContainerRunner containerRunner =
				new ContainerRunner.Builder(image)
					.directory(webDir.toFile())
					.addEnvVars(envVars)
					.publishedPort(port)
					.build()
		ContainerRunningResult crrP = containerRunner.run()
		if (crrP.returncode() != 0) {
			KeywordUtil.markFailedAndStop("failure occured while starting Flaskr server at port#${port}.\n"
				+ crrP.toString())
		}
	}


	/**
	 * shutdown the process of Flaskr
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		if (testCaseContext.getTestCaseId().contains("Flaskr/VisualInspectionTwins")) {
			shutdownFlaskr(FLASKR_PROD_PORT)
			shutdownFlaskr(FLASKR_DEV_PORT)
		}
	}

	/*
	 *
	 */
	void shutdownFlaskr(PublishedPort publishedPort) {
		ContainerFindingResult cfr = ContainerFinder.findContainerByHostPort(publishedPort)
		if (cfr.returncode() == 0) {
			ContainerId containerId = cfr.containerId()
			ContainerStoppingResult csr = ContainerStopper.stopContainer(containerId)
			if (csr.returncode() != 0) {
				KeywordUtil.markFailedAndStop(
					"failure occured while shutting down Flaskr server at port#${publishedPort.hostPort()}.\n"
					+ cfr.toString())
			}
		} else {
			KeywordUtil.markFailedAndStop(
				"unabled to find a Docker container which is listening to the port#${publishedPort.hostPort()}.\n"
				+ cfr.toString())
		}
	}

}
