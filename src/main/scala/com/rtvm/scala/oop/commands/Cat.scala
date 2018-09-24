package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.filesystem.State

/**
  * - Read and print file
  * - No support for absolute or relative path
  *
  * @param filename File to read
  */
class Cat(filename: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd
    val dirEntry = wd.findEntry(filename)

    if (dirEntry == null || !dirEntry.isFile)
      state.setMessage(filename + ": no such file")
    else
      state.setMessage(dirEntry.asFile.contents)
  }
}
