android.Plugin.androidBuild
platformTarget in Android := "android-21"

name := "macroid-akka-pingpong"

scalaVersion := "2.11.4"
javacOptions ++= Seq("-target", "1.7", "-source", "1.7") // so we can build with java8

// a shortcut
run <<= run in Android

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com"
)

// add linter
scalacOptions in (Compile, compile) ++=
  (dependencyClasspath in Compile).value.files.map("-P:wartremover:cp:" + _.toURI.toURL) ++
  Seq("-P:wartremover:traverser:macroid.warts.CheckUi")

libraryDependencies ++= Seq(
  aar("org.macroid" %% "macroid" % "2.0.0-M4"),
  "com.android.support" % "support-v4" % "22.1.1",
  "org.macroid" %% "macroid-akka" % "2.0.0-M4",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  compilerPlugin("org.brianmckenna" %% "wartremover" % "0.11")
)

proguardScala in Android := true

// Generic ProGuard rules
proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic"
)

// ProGuard rules for Akka
proguardOptions in Android ++= Seq(
  "-keep class akka.actor.Actor$class { *; }",
  "-keep class akka.actor.LightArrayRevolverScheduler { *; }",
  "-keep class akka.actor.LocalActorRefProvider { *; }",
  "-keep class akka.actor.CreatorFunctionConsumer { *; }",
  "-keep class akka.actor.TypedCreatorFunctionConsumer { *; }",
  "-keep class akka.dispatch.BoundedDequeBasedMessageQueueSemantics { *; }",
  "-keep class akka.dispatch.UnboundedMessageQueueSemantics { *; }",
  "-keep class akka.dispatch.UnboundedDequeBasedMessageQueueSemantics { *; }",
  "-keep class akka.dispatch.DequeBasedMessageQueueSemantics { *; }",
  "-keep class akka.dispatch.MultipleConsumerSemantics { *; }",
  "-keep class akka.actor.LocalActorRefProvider$Guardian { *; }",
  "-keep class akka.actor.LocalActorRefProvider$SystemGuardian { *; }",
  "-keep class akka.dispatch.UnboundedMailbox { *; }",
  "-keep class akka.actor.DefaultSupervisorStrategy { *; }",
  "-keep class macroid.akka.AkkaAndroidLogger { *; }",
  "-keep class akka.event.Logging$LogExt { *; }"
)
