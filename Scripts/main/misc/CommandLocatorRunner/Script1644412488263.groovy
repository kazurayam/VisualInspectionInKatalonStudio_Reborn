import com.kazurayam.subprocessj.CommandLocator
import com.kazurayam.subprocessj.CommandLocator.CommandLocatingResult

CommandLocatingResult clr = CommandLocator.find("docker")
println clr.toString()
