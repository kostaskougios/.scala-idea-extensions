package unittests.scripts

/**
 * @author	kostas.kougios
 *            Date: 04/08/14
 */
class A extends (String => String)
{
	override def apply(s: String) = s + "-OK"
}
