package experiments

import java.io.{ByteArrayOutputStream, File, PrintStream}

import org.apache.maven.cli.MavenCli

/**
 * @author	kostas.kougios
 *            Date: 23/07/14
 */
object Mvn extends App
{
	val cli = new MavenCli
	val stdOut = new ByteArrayOutputStream
	val stdErr = new ByteArrayOutputStream
	val outStream = new PrintStream(stdOut, true)
	val errStream = new PrintStream(stdErr, true)
	try {
		cli.doMain(Array("-v"), new File("/home/ariskk/IdeaProjects/mytest").getAbsolutePath, outStream, errStream)
	} finally {
		outStream.close()
		errStream.close()
	}

	println(new String(stdOut.toByteArray, "UTF-8"))
	println(new String(stdErr.toByteArray, "UTF-8"))
}
