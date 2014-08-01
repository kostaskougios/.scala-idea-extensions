package compilation

import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.{VirtualFile, VirtualFileEvent}
import org.scalaideaextension.compilation.ProjectCompilationListener
import org.scalaideaextension.eventlog.EventLog
import org.scalaideaextension.vfs.{ChangeTracker, VFSChangeListener}

/**
 * monitors file changes in modules, and if a source file changed, upon module compilation, it triggers
 * a mvn build.
 *
 * Also it triggers a mvn build if the user rebuild the project
 *
 * @author	kostas.kougios
 *            Date: 22/07/14
 */
class ProjectCompiled extends ProjectCompilationListener with VFSChangeListener
{

	import compilation.ProjectCompiled._

	override def success(context: CompileContext) = {
		val affectedModules = context.getCompileScope.getAffectedModules
		for (module <- affectedModules if (context.isRebuild || changeTracker.isTrackedSinceLastCall(module))) {
			val path = module.getModuleFile.getParent.getPath
			EventLog.info(this, s"do something for modified module at ${path}")
		}
	}

	override def contentsChanged(module: Module, event: VirtualFileEvent) = {
		val file = event.getFile
		if (isSource(file)) {
			EventLog.trace(this, s"module ${module}, source file modification detected : ${file}")
			changeTracker.track(module, file)
		}
	}

	def isSource(file: VirtualFile): Boolean = file match {
		case null => false
		case f if (f.getName == "src") => true
		case f => isSource(f.getParent)
	}
}

object ProjectCompiled
{
	private val changeTracker = ChangeTracker.empty
}