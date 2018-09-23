package com.rtvm.scala.oop.commands

import com.rtvm.scala.oop.files.{DirEntry, Directory}
import com.rtvm.scala.oop.filesystem.State

/**
  * Create Directory or File
  *
  * @param name Directory or File name to create
  */
abstract class CreateEntry(name: String) extends Command {

  /**
    * Change state by CreateEntry
    *
    * @param state previous shell state
    * @return next shell state by CreateEntry command
    */
  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    }
    else if (name.contains(Directory.SEPARATOR)) {
      // mkdir some/inside
      state.setMessage(name + " must not contain separators!")
    }
    else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    }
    else {
      doCreateEntry(state, name)
    }
  }

  /**
    * Check for directory or file name
    *
    * @param name Directory or file name to create
    * @return true = Illegal
    */
  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  /**
    * Create Dir or File in right place
    *
    * @param state Current shell state
    * @param name Dir or File name to create
    * @return Next shell state
    */
  def doCreateEntry(state: State, name: String): State = {

    /**
      * Update all ancestor directories on path to the folder which insert new file or directory
      * ----------------------------------------------------------------------------------------
      * example:
      *
      * /a/b
      * (contents)
      * (new entry) /e
      *
      * newRoot = updateStructure(root, ["a","b"], /e)
      * => path.isEmpty?
      * => oldEntry = /a
      *           root.replaceEntry("a", ["b"], /e)
      * => path.isEmpty?
      * => oldEntry = /b
      * /a.replaceEntry("b", [], /e)
      * => path.isEmpty => /b.add(/e)
      * ----------------------------------------------------------------------------------------
      *
      * @param currentDirectory Start from root and traverse to the end directory of path
      * @param path Remaining path from current dir
      * @param newEntry New dir or file to make
      * @return Root directory
      */
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    // 1. all the directories in the full path ("/a/b/c/d" => ["a","b","c","d"])
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. create new directory entry in the wd
    val newEntry: DirEntry = createSpecificEntry(state)

    // 3. update the whole directory structure starting from the root
    // (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find new working directory INSTANCE given wd's full path,  in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  /**
    * Create file or directory
    *
    * @param state current shell state
    * @return New empty directory or file
    */
  def createSpecificEntry(state: State): DirEntry
}
