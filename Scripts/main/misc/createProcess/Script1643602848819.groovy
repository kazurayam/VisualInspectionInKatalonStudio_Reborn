import java.nio.file.Files
import java.util.Arrays
import com.kazurayam.subprocessj.ProcessKiller

Integer FLASKR_PROD_PORT = 3080
Integer FLASKR_DEV_PORT  = 3090

File prodDir = Files.createTempDirectory("TL_Flaskr_prod").toFile()

startupFlaskr(prodDir, FLASKR_PROD_PORT, false)

//shutdownFlaskr(FLASKR_PROD_PORT)

void startupFlaskr(File directory, Integer port, Boolean altView = false) {
	Objects.requireNonNull(directory)
	try {
		String[] args = [
			"/usr/local/bin/docker", "run", "-it",
			"-e", "FLASKR_ALT_VIEW=\"${altView}\"",
			"-p", "${port}:8080",
			"kazurayam/flaskr-kazurayam:1.1.0"
			] as String[]
		ProcessBuilder pb = new ProcessBuilder(args)
		pb.directory(directory)
		//Process process = pb.start();
	} catch (Exception e) {
		e.printStackTrace()
	}
}

void shutdownFlaskr(Integer port) {
	try {
		Long processId = ProcessKiller.killProcessOnPort(port)
	} catch (Exception e) {
		e.printStackTrace()
	}
}