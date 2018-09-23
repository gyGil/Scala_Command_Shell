package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.files.{Directory, File}
import com.rtvm.scala.oop.filesystem.State

import scala.annotation.tailrec

/**
  * Echo (ex. echo something => print something)
  *           echo text > file => create file
  *           echo text >> file => append to file
  * @param args after echo
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
        doEcho(state, contents, filename, append=true)
      else if(">".equals(operator))
        doEcho(state, contents, filename, append=false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  /**
    * Add new file or append content to file recursively
    *
    * @param currentDirectory
    * @param path After currentDirectory
    * @param contents
    * @param append true: append mode
    * @return Root dir
    */
  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if (dirEntry == null) currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else
        if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
    }
    else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  /**
    * Create file with content or Append content to the file
    *
    * @param state
    * @param contents Content
    * @param filename Target file (not support path type. ex. /a/b.txt)
    * @param append Append to file or not
    * @return next state
    */
  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {
    if (filename.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ filename, contents, append)
      if (newRoot == state.root)
        state.setMessage(filename + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
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
