import org.jetbrains.sbtidea.Keys.*
import org.jetbrains.sbtidea.verifier.FailureLevel

lazy val scala3Version = "3.3.0"

lazy val intellijVersion =
  "231.9392.1" // https://youtrack.jetbrains.com/articles/IDEA-A-2100661425/IntelliJ-IDEA-2023.1-Latest-Builds
lazy val pluginVersion = s"0.1.0-$intellijVersion-M3"

ThisBuild / version := pluginVersion

lazy val jacksonVersion = "2.13.3"

inThisBuild(
  List(
    homepage := Some(url("https://github.com/bitlap/intellij-sbt-dependency-analyzer")),
    developers := List(
      Developer(
        id = "jxnu-liguobin",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://blog.dreamylost.cn")
      ),
      Developer(
        id = "IceMimosa",
        name = "IceMimosa",
        email = "chk19940609@gmail.com",
        url = url("http://patamon.me")
      )
    )
  )
)

lazy val `sbt-dependency-analyzer` = (project in file("."))
  .enablePlugins(SbtIdeaPlugin)
  .settings(
    scalaVersion                   := scala3Version,
    organization                   := "org.bitlap",
    version                        := (ThisBuild / version).value,
    ThisBuild / intellijPluginName := "Sbt Dependency Analyzer",
    ThisBuild / intellijBuild      := intellijVersion,
    ThisBuild / intellijPlatform   := (Global / intellijPlatform).??(IntelliJPlatform.IdeaCommunity).value,
    signPluginOptions := signPluginOptions.value.copy(
      enabled = true,
      certFile = Some(file("/Users/liguobin/chain.crt")),        // or via PLUGIN_SIGN_KEY env var
      privateKeyFile = Some(file("/Users/liguobin/private.pem")) // or via PLUGIN_SIGN_CERT env var
//      keyPassphrase =
//        Some("xxx") // or None if password is not set(or via PLUGIN_SIGN_KEY_PWD env var)
    ),
    pluginVerifierOptions := pluginVerifierOptions.value.copy(
      version = "1.301",                                      // use a specific verifier version
      offline = true,                                         // forbid the verifier from reaching the internet
      failureLevels = Set(FailureLevel.DEPRECATED_API_USAGES) // only fail if deprecated APIs are used
    ),
    Global / intellijAttachSources := true,
    intellijPlugins ++= Seq("com.intellij.java", "com.intellij.java-i18n", "org.intellij.scala").map(_.toPlugin),
    Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "resources",
    Test / unmanagedResourceDirectories += baseDirectory.value / "src" / "test" / "resources",
    patchPluginXml := pluginXmlOptions { xml =>
      xml.version = pluginVersion
      xml.pluginDescription = IO.read(baseDirectory.value / "src" / "main" / "resources" / "patch" / "description.html")
      xml.changeNotes = IO.read(baseDirectory.value / "src" / "main" / "resources" / "patch" / "change.html")
    },
    publish / skip := true,
    commands ++= Commands.value,
    libraryDependencies ++= Seq(
      "guru.nidi"                  % "graphviz-java"   % "0.18.1",
      "org.graalvm.js"             % "js"              % "23.0.1",
      "com.fasterxml.jackson.core" % "jackson-core"    % jacksonVersion,
      "org.scalatest"             %% "scalatest"       % "3.2.16" % Test,
      "ch.qos.logback"             % "logback-classic" % "1.2.3"
    )
  )
