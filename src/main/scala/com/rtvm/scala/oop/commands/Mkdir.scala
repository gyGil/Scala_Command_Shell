package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.files.{DirEntry, Directory}
import com.rtvm.scala.oop.filesystem.State

/**
  * mkdir command
  *
  * @param name Directory or File name to create
  */
class Mkdir(name: String) extends CreateEntry(name) {

  /**
    * Create new empty dir
    *
    * @param state current shell state
    * @return New empty directory or file
    */
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
