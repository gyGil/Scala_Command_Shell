package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.files.{DirEntry, Directory}
import com.rtvm.scala.oop.filesystem.State

import scala.annotation.tailrec

/**
  * cd Command
  *
  * @param dir (ex: /a/b/c, a/../d/)
  */
class Cd(dir: String) extends Command {


  /**
    * Change state by cd command
    *
    * @param state before cd command
    * @return state after cd command
    */
  override def apply(state: State): State = {

    val root = state.root
    val wd = state.wd

    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir

    val destinationDirectory = doFindEntry(root, absolutePath)

    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + ": no such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }

  /**
    * Find destination directory
    *
    * @param root root directory
    * @param path absolute path of destination directory
    * @return destination directory or null(not found)
    */
  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry =
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)

        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }

    /**
      * Remove "." and ".." tokens
      * ".": just remove "." token only (not affect to final path)
      * "..": remove ".." with prior token
      *
      * @param path Asolute full path
      * @param result Accumulator to store final full path
      * @return
      */
    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      if (path.isEmpty) result
      else if (path.head.equals(".")) collapseRelativeTokens(path.tail, result)
      else if (path.head.equals(".."))
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    val newTokens = collapseRelativeTokens(tokens, List())
    if (newTokens == null) null
    else findEntryHelper(root, newTokens)
  }
}
