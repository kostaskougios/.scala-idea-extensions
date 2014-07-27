package compilation

import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFileEvent
import maven.Mvn
import org.scalaideaextension.compilation.ProjectCompilationListener
import org.scalaideaextension.eventlog.EventLog
import org.scalaideaextension.vfs.{ChangeTracker, VFSChangeListener}

/**
 * @author	kostas.kougios
 *            Date: 22/07/14
 */
class ProjectCompiled extends ProjectCompilationListener with VFSChangeListener
{
	private val changeTracker = ChangeTracker.empty

	override def success(context: CompileContext) = {
		val affectedModules = context.getCompileScope.getAffectedModules
		for (module <- affectedModules) {
			val path = module.getModuleFile.getParent.getPath
			val result = Mvn(path, Array("clean", "package", "-DskipTests"))
			if (result.returnCode != 0) EventLog.error(this, s"mvn returned with status code ${result}", null)
			EventLog.trace(this, result.log)
		}
	}

	override def contentsChanged(module: Module, event: VirtualFileEvent) = {
		//		ModuleManager.getInstance()
		//		ModuleUtil.findModuleForFile()
		//		changeTracker.track(event.getFile.getFileSystem.)
	}
}
