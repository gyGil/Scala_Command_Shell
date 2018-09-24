package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.filesystem.State

trait Command extends (State => State){

  //def apply(state: State): State  // Change state
}

/**
  * - Command for factory of empty/incomplete commands
  * - Select proper command from input
  */
object Command {
  val MKDIR = "mkdir"
  val TOUCH = "touch"
  val LS = "ls"
  val PWD = "pwd"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  /**
    * Factory for Empty Command
    *
    * @return Empty command
    */
  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state
  }

  /**
    * Incompleted Command
    *
    * @param name Command name
    * @return Same state as before
    */
  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State =
      state.setMessage(name + ": incomplete command!")
  }

  /**
    * Build Command from input string
    *
    * @param input Command input from user
    * @return Command which generated from user input
    */
  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")

    if (input.isEmpty || tokens.isEmpty) emptyCommand
    else tokens(0) match {
      case MKDIR =>
        if (tokens.length < 2) incompleteCommand(MKDIR)
        else new Mkdir(tokens(1))
      case LS => new Ls
      case PWD => new Pwd
      case TOUCH =>
        if (tokens.length < 2) incompleteCommand(TOUCH)
        else new Touch(tokens(1))
      case CD =>
        if (tokens.length < 2) incompleteCommand(CD)
        else new Cd(tokens(1))
      case RM =>
        if (tokens.length < 2) incompleteCommand(RM)
        else new Rm(tokens(1))
      case ECHO =>
        if (tokens.length < 2) incompleteCommand(ECHO)
        else new Echo(tokens.tail)
      case CAT =>
        if (tokens.length < 2) incompleteCommand(CAT)
        else new Cat(tokens(1))
      case _ => new UnknownCommand
    }
  }
}
