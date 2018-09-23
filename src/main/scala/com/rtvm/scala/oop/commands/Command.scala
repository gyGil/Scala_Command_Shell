package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.filesystem.State

trait Command {

  def apply(state: State): State  // Change state
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

    else if(MKDIR.equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand(MKDIR)
      else new Mkdir(tokens(1))

    else if(LS.equals(tokens(0)))
      new Ls

    else if(PWD.equals(tokens(0)))
      new Pwd

    else if (TOUCH.equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand(TOUCH)
      else new Touch(tokens(1))

    else if (CD.equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand(CD)
      else new Cd(tokens(1))

    else if (RM.equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand(RM)
      else new Rm(tokens(1))

    else if (ECHO.equals(tokens(0)))
      if (tokens.length < 2) incompleteCommand(ECHO)
      else new Echo(tokens.tail)

    else new UnknownCommand
  }
}
