package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.filesystem.State

import scala.annotation.tailrec

/**
  * Echo (ex. echo something => print something)
  *           echo text > file => create file
  *           echo text >> file => append to file
  * @param args
  */
class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {

    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val contents = createContent(args, args.length - 2)

      if (">>".equals(operator))
        doEcho(state, contents, filename, true)
      else if(">".equals(operator))
        doEcho(state, contents, filename, false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  /**
    * Create file with content or Append content to the file
    *
    * @param state
    * @param contents Content
    * @param filename Target file
    * @param append Append to file or not
    * @return next state
    */
  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {
    ???
  }

  /**
    * Create content to save to file
    *
    * @param args
    * @param topIndex The last index of content + 1
    * @return Content
    */
  def createContent(args: Array[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String =
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))

    createContentHelper(0, "")
  }
}
