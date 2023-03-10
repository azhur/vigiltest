ThisBuild / scalafmtCheck    := true
ThisBuild / scalafmtSbtCheck := true

// ### Aliases ###
addCommandAlias("checkFmt", "scalafmtCheckAll")
addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "io.azhur",
      scalaVersion := "2.12.13"
    )
  ),
  name                     := "vigiltest",
  version                  := "0.0.1",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
  scalacOptions ++= Seq("-deprecation", "-unchecked"),
  Test / parallelExecution := false,
  fork                     := true,
  libraryDependencies ++= Seq(
    "com.github.scopt" %% "scopt"              % "4.1.0",
    "org.apache.spark" %% "spark-sql"          % "3.3.2",
    "org.apache.hadoop" % "hadoop-aws"         % "3.3.4",
    "org.scalatest"    %% "scalatest"          % "3.2.15"      % "test",
    "org.scalacheck"   %% "scalacheck"         % "1.17.0"      % "test",
    "com.holdenkarau"  %% "spark-testing-base" % "3.3.1_1.4.0" % "test"
  ),

  // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
  Compile / run        := Defaults
    .runTask(Compile / fullClasspath, mainClass in (Compile, run), runner in (Compile, run))
    .evaluated,
  scalacOptions ++= Seq("-deprecation", "-unchecked"),
  pomIncludeRepository := { x => false },
  resolvers ++= Seq(
    "sonatype-releases".at("https://oss.sonatype.org/content/repositories/releases/"),
    "Typesafe repository".at("https://repo.typesafe.com/typesafe/releases/"),
    "Second Typesafe repo".at("https://repo.typesafe.com/typesafe/maven-releases/")
  ) ++ Resolver.sonatypeOssRepos("public"),
  pomIncludeRepository := { _ => false }
)
