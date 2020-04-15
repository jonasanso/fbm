import ammonite.ops._
import mill._
import mill.scalalib._
import mill.util.Ctx


object fbm extends ScalaModule with GraalVM {
  def scalaVersion = "2.12.8"

  // compile flags
  def scalacOptions = Seq(
    "-feature",
    "-deprecation",
    "-Ypartial-unification",
    "-Ywarn-value-discard"
  )

  // entry point
  def mainClass = Some("nlp.fbm.Main")

  // dependency versions
  val breeze = "1.0"
  val refined = "0.9.13"

  // dependencies
  override def ivyDeps = Agg(
    ivy"eu.timepit::refined-scopt:0.9.13",
    ivy"org.scalanlp::breeze:$breeze",
    ivy"org.scalanlp::breeze-natives:$breeze"
  )

  def native = T {
    buildNative(assembly(), "fbm")
  }

  object test extends Tests {
    override def ivyDeps = Agg(ivy"com.lihaoyi::utest::0.6.3")

    def testFrameworks = T(Seq("utest.runner.Framework"))
  }

}

trait GraalVM {
  def buildNative(jar: PathRef, name: String)(implicit ctx: Ctx.Dest): Unit = {
    val jarName = s"$name.jar"
    cp(jar.path, ctx.dest / jarName)
    Shellout.executeStream(ctx.dest, Command(Vector("native-image", "-jar", jarName), Map.empty, Shellout.executeStream)) match {
      case CommandResult(0, s) => s.foreach(_.left.foreach(print))
      case c@CommandResult(e, s) =>
        throw ShelloutException(c)
    }
  }
}

