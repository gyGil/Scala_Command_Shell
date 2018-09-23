package com.rtvm.scala.oop.commands
import com.rtvm.scala.oop.files.Directory
import com.rtvm.scala.oop.filesystem.State

/**
  * Remove file or folder having name arg
  *
  * @param name File or dir name with or without relative path
  */
class Rm(name: String) extends Command {

  /**
    * Remove file and change shell state
    *
    * @param state Shell state before remove
    * @return Shell state after remove
    */
  override def apply(state: State): State = {
    val wd = state.wd
    val absolutePath =
      if (name.startsWith(Directory.SEPARATOR)) name
      else if (wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name

    if (Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Doesn't allow to remove root!")
    else
      doRm(state, absolutePath)

  }

  /**
    * Remove file or dir
    *
    * @param state Current Shell state
    * @param path Absolute path to file or dir to remove
    * @return Shell state after removing or fail to remove
    */
  def doRm(state: State, path: String): State = {

    /**
      * Rebuild all directories on path recursively after removing target file or dir
      * If it is fail, it doesn't rebuild at all
      *
      * @param currentDirectory
      * @param path Following path after currentDirectory
      * @return Rebuilded dir or same dir before
      */
    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {

      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)

        if (!nextDirectory.isDirectory) currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDirectory == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root)
      state.setMessage(path + ": no such file or directory")
    else
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }
}
