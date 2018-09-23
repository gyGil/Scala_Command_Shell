package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.files.{DirEntry, File}
import com.rtvm.scala.oop.filesystem.State

/**
  * touch command
  * @param name File name to create
  */
class Touch(name: String) extends CreateEntry(name) {

  /**
    * Create empty file
    *
    * @param state current shell state
    * @return New empty directory or file
    */
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
