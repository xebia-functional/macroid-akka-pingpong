package com.example.pingpong

import akka.actor.{Props, ActorLogging, ActorRef}
import macroid.akkafragments.FragmentActor

object RacketActor {
  case object Ball
  case object Smash

  // this is a common Akka pattern: http://doc.akka.io/docs/akka/snapshot/scala/actors.html
  // IMPORTANT: notice how we use `new RacketActor` and not `Props[RacketActor]`
  // this forces ProGuard to keep the class `RacketActor`
  // you might add a ProGuard rule as well though
  def props = Props(new RacketActor)
}

/** The actor that handles the ball */
class RacketActor extends FragmentActor[RacketFragment] with ActorLogging {
  import RacketActor._
  import FragmentActor._

  var lastOpponent: Option[ActorRef] = None

  // receiveUi handles attaching and detaching UI
  // and then (sic!) passes the message to us
  def receive = receiveUi andThen {
    case Ball ⇒
      // boast
      log.debug("got the ball!")
      // save the opponent reference
      lastOpponent = Some(sender)
      // notify the UI
      withUi(f ⇒ f.receive)

    case Smash ⇒
      // boast
      log.debug("smash!!!")
      // send the ball to the opponent
      lastOpponent.foreach(_ ! Ball)
      // forget who it was
      lastOpponent = None

    case AttachUi(_) ⇒
      // if the ui is attached after
      // receiveing the ball, update it
      lastOpponent foreach { _ ⇒
        withUi(f ⇒ f.receive)
      }

    case DetachUi ⇒
      // do nothing
  }
}
