package com.example.pingpong

import android.os.Bundle
import android.widget.LinearLayout
import android.view.ViewGroup.LayoutParams._
import android.support.v4.app.FragmentActivity

// import macroid stuff
import macroid._
import macroid.FullDsl._
import macroid.akkafragments.AkkaActivity

/** The main activity */
class MainActivity extends FragmentActivity with Contexts[FragmentActivity] with IdGeneration with AkkaActivity {
  // name of our actor system
  val actorSystemName = "pingpong"

  // players
  lazy val ping = actorSystem.actorOf(RacketActor.props, "ping")
  lazy val pong = actorSystem.actorOf(RacketActor.props, "pong")

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    // initialize the actors
    (ping, pong)

    // layout params
    val lps = lp[LinearLayout](MATCH_PARENT, WRAP_CONTENT, 1.0f)

    // include the two fragments
    val view = l[LinearLayout](
      // we pass a name for the actor, and id+tag for the fragment
      f[RacketFragment].pass("name" → "ping").framed(Id.ping, Tag.ping) <~ lps,
      f[RacketFragment].pass("name" → "pong").framed(Id.pong, Tag.pong) <~ lps
    ) <~ vertical

    setContentView(getUi(view))
  }

  override def onStart() = {
    super.onStart()

    // start the game
    ping.tell(RacketActor.Ball, pong)
  }
}
