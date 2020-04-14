import mill._
import mill.scalalib._

object fbm extends ScalaModule {
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
  val scallop = "3.1.2"

  // dependencies
  def ivyDeps = Agg(
    ivy"org.rogach::scallop:$scallop",
    ivy"eu.timepit::refined:0.9.13",
    ivy"org.scalanlp::breeze:$breeze",
    ivy"org.scalanlp::breeze-natives:$breeze"
  )

  object test extends Tests{
    def ivyDeps = Agg(ivy"com.lihaoyi::utest::0.6.3")
    def testFrameworks = Seq("utest.runner.Framework")
  }
}
