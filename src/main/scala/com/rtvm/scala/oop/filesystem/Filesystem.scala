package com.rtvm.scala.oop.filesystem

import java.util.Scanner

import com.rtvm.scala.oop.commands.Command
import com.rtvm.scala.oop.files.Directory

object Filesystem extends App{

  val root = Directory.ROOT
  var state = State(root, root)  // *NOTE* VAR
  val scanner = new Scanner(System.in)

  while(true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

}
