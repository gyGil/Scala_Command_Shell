package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.filesystem.State

/**
  * pwd command
  */
class Pwd extends Command {

  /**
    * Print out current path
    *
    * @param state
    * @return
    */
  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
