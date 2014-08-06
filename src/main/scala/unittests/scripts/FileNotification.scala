package unittests.scripts

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFileEvent
import org.scalaideaextension.vfs.VFSChangeListener

/**
 * @author	kostas.kougios
 *            Date: 06/08/14
 */
class FileNotification extends VFSChangeListener
{
	var lastChange: Option[(Module, VirtualFileEvent)] = None

	override def contentsChanged(module: Module, event: VirtualFileEvent) {
		lastChange = Some(module, event)
	}
}
