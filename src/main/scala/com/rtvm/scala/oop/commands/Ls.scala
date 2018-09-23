package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.files.DirEntry
import com.rtvm.scala.oop.filesystem.State

/**
  * LS command to show list of dir and file in current path
  */
class Ls extends Command {

  /**
    * change to same state with file and dir list
    *
    * @param state Before ls
    * @return state After ls
    */
  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutput(contents)
    state.setMessage(niceOutput)
  }

  /**
    * Create nice ls output
    * @param contents ls list
    * @return Formatted text of ls output
    */
  def createNiceOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]\n" + createNiceOutput(contents.tail)
    }
  }
}
