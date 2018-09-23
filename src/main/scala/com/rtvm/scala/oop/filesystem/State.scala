package com.rtvm.scala.oop.filesystem

import com.rtvm.scala.oop.files.Directory

/**
  * Shell State containing output from previous state(executed by previous command)
  *
  * @param root path /
  * @param wd absolute working directory
  * @param output Output message of previous Command on previous State
  */
class State(val root: Directory, val wd: Directory, val output: String) {

  /**
    * Print output to shell
    */
  def show: Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }

  /**
    * Set message from previous command on previous state
    *
    * @param message Output message
    * @return new State
    */
  def setMessage(message: String): State =
    State(root, wd, message)
}

object State {
  val SHELL_TOKEN = "$ "

  // output from previous state
  def apply(root: Directory, wd: Directory, output: String = ""): State =
    new State(root, wd, output)
}
