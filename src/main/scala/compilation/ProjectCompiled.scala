package compilation

import com.intellij.openapi.compiler.CompileContext
import maven.Mvn
import org.scalaideaextension.compilation.ProjectCompilationListener
import org.scalaideaextension.eventlog.EventLog

/**
 * @author	kostas.kougios
 *            Date: 22/07/14
 */
class ProjectCompiled extends ProjectCompilationListener
{
	override def success(context: CompileContext) = {
		EventLog.trace(this, "success()")
		val project = context.getProject
		val path = project.getBasePath

		Mvn(path, Array("clean", "package", "-DskipTests"))
	}
}
