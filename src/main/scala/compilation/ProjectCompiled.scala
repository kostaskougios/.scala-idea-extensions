package compilation

import java.io.{ByteArrayOutputStream, PrintStream}

import com.intellij.openapi.compiler.CompileContext
import org.apache.maven.cli.MavenCli
import org.kostaskougios.idea.eventlog.EventLog
import org.scalaideaextension.compilation.ProjectCompilationListener

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

		mvnRun(path)
	}

	def mvnRun(path: String) {
		val classLoader = Thread.currentThread.getContextClassLoader
		try {
			Thread.currentThread.setContextClassLoader(getClass.getClassLoader)
			mvnRunInner(path)
		} finally {
			Thread.currentThread.setContextClassLoader(classLoader)
		}
	}

	def mvnRunInner(path: String) {
		val cli = new MavenCli

		val stdOut = new ByteArrayOutputStream
		val stdErr = new ByteArrayOutputStream
		val outStream = new PrintStream(stdOut, true)
		val errStream = new PrintStream(stdErr, true)
		EventLog.trace(this, s"executing mvn for path ${path}")
		val start = System.currentTimeMillis
		try {
			val result = cli.doMain(Array("clean", "install"), path, outStream, errStream)
			if (result == 1) EventLog.error(this, "mvn reported an illegal argument", null)
		} finally {
			outStream.close()
			errStream.close()
		}

		val stop = System.currentTimeMillis

		val msg =
			s"""
			  |Maven Execution completed in ${stop - start} ms.
			  |StdOut:
			  |${new String(stdOut.toByteArray, "UTF-8")}
			  |StdErr:
			  |${new String(stdErr.toByteArray, "UTF-8")}
			""".stripMargin

		EventLog.info(this, msg)
	}
}
