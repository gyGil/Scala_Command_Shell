package com.rtvm.scala.oop.filesystem

import java.util.Scanner

import com.rtvm.scala.oop.commands.Command
import com.rtvm.scala.oop.files.Directory

object Filesystem extends App{

  val root = Directory.ROOT

  /*
  List(1,2,3,4).foldLeft(0)((x,y) => x + y)
  [1,2,3,4]
  0 (op) 1 => 1
  1 (op) 2 => 3
  3 (op) 3 => 6
  6 (op) 4 => 10
   */
  io.Source.stdin.getLines().foldLeft(State(root, root).show)((currentState, newLine) => {
    //currentState.show
    Command.from(newLine).apply(currentState).show
  })

//  var state = State(root, root)  // *NOTE* VAR
//  val scanner = new Scanner(System.in)
//
//  while(true) {
//    state.show
//    val input = scanner.nextLine()
//    state = Command.from(input).apply(state)
//  }

}
