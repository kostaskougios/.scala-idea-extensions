package maven

import java.io.{ByteArrayOutputStream, PrintStream}

import org.apache.maven.cli.MavenCli
import org.scalaideaextension.eventlog.EventLog

/**
 * @author	kostas.kougios
 *            Date: 25/07/14
 */
object Mvn
{
	def apply(path: String, args: Array[String]): MvnResult = {
		val classLoader = Thread.currentThread.getContextClassLoader
		try {
			Thread.currentThread.setContextClassLoader(getClass.getClassLoader)
			mvnRunInner(path, args)
		} finally {
			Thread.currentThread.setContextClassLoader(classLoader)
		}
	}

	private def mvnRunInner(path: String, args: Array[String]) = {
		val cli = new MavenCli

		val stdOut = new ByteArrayOutputStream
		val stdErr = new ByteArrayOutputStream
		val outStream = new PrintStream(stdOut, true)
		val errStream = new PrintStream(stdErr, true)
		EventLog.trace(this, s"executing mvn for path ${path}")
		val start = System.currentTimeMillis
		val result = try {
			cli.doMain(args, path, outStream, errStream)
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

		MvnResult(result, msg)
	}

	case class MvnResult(returnCode: Int, log: String)

}
