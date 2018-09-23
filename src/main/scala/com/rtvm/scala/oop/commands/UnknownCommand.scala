package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.filesystem.State

/**
  * Unknown command.
  */
class UnknownCommand extends Command{

  /**
    * Print error message.
    *
    * @param state
    * @return
    */
  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
