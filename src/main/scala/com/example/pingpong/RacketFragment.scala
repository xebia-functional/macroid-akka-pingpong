package com.example.pingpong

import android.view.{Gravity, ViewGroup, LayoutInflater}
import android.os.Bundle
import android.widget.{FrameLayout, Button}
import android.view.ViewGroup.LayoutParams._

import macroid._
import macroid.FullDsl._
import macroid.contrib.ExtraTweaks._
import macroid.util.Ui
import macroid.akkafragments.AkkaFragment

import scala.concurrent.ExecutionContext.Implicits.global

/** Styles for our widgets */
object Styles {
  // how racket looks
  def racket(implicit appCtx: AppContext) =
    hide + disable +
    text("SMASH") +
    TextSize.large +
    lp[FrameLayout](WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
}

/** Effects for out widgets */
object Effects {
  // make a glorious fade
  def appear =
    fadeIn(600) +
    enable

  // disappear with style
  def disappear =
    disable ++
    fadeOut(600) ++
    delay(600)
}

/** Our UI fragment */
class RacketFragment extends AkkaFragment with Contexts[AkkaFragment] {
  // get actor name from arguments
  lazy val actorName = getArguments.getString("name")

  // actor for this fragment
  lazy val actor = Some(actorSystem.actorSelection(s"/user/$actorName"))

  // a slot for the racket button
  var racket = slot[Button]

  // trigger the fadeIn effect
  def receive =
    racket <~ Effects.appear

  // smash the ball
  def smash =
    // wait until the racket disappears
    (racket <~~ Effects.disappear) ~~
    // tell the actor to smash
    Ui(actor.foreach(_ ! RacketActor.Smash))

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle) = getUi {
    l[FrameLayout](
      w[Button] <~ wire(racket) <~ Styles.racket <~ On.click(smash)
    )
  }
}
