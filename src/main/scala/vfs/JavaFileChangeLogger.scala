package vfs

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFileEvent
import org.scalaideaextension.vfs.VFSChangeListener

/**
 * @author	kostas.kougios
 *            Date: 21/07/14
 */
class JavaFileChangeLogger extends VFSChangeListener
{
	override def contentsChanged(module: Module, event: VirtualFileEvent) {
		//		EventLog.info(this, s"Notified about ${event}")
	}
}
