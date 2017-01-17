enablePlugins(AndroidApp)
platformTarget in Android := "android-25"

name := "macroid-akka-pingpong"

scalaVersion := "2.11.8"
javacOptions ++= Seq("-target", "1.7", "-source", "1.7") // so we can build with java8

// a shortcut
run <<= run in Android

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com"
)

libraryDependencies ++= Seq(
  aar("org.macroid" %% "macroid" % "2.0"),
  "com.android.support" % "support-v4" % "25.1.0",
  "org.macroid" %% "macroid-akka" % "2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.16"
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


fork in run := true
