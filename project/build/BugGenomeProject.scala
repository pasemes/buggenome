import sbt._

class BugGenomeProject(info: ProjectInfo) extends ParentProject(info) with IdeaProject {

   lazy val exceptionmatcher = project("exceptionmatcher", "Core", new ExceptionMatcherProject(_))
   class ExceptionMatcherProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
       val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7" % "test" withSources
       val mockitoAll = "org.mockito" % "mockito-all" % "1.8.5" % "test" withSources
       val logback = "ch.qos.logback" % "logback-classic" % "0.9.27"
       val slf4s = "com.weiglewilczek.slf4s" %% "slf4s" % "1.0.3"
       val casbah = "com.mongodb.casbah" % "casbah_2.8.1" % "2.1.2" extra("docUrl" -> "http://api.mongodb.org/scala/casbah/2.1.1/scaladoc/")
   }

   lazy val restinterface = project("restinterface", "REST Interface", new DefaultProject(_) with IdeaProject, exceptionmatcher)
   lazy val webgui = project("webgui", "Web GUI", new DefaultProject(_) with IdeaProject) //web doesn't have any deps because it should use the REST interface 
}
