package com.rtvm.scala.oop.files

import com.rtvm.scala.oop.filesystem.FilesystemException
import scala.annotation.tailrec

/**
  * Directory
  *
  * @param parentPath Absolute path of parent path
  * @param name Name of current dir
  * @param contents list of file and dir in current dir
  */
class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

  def getType: String = "Directory"

  def isFile: Boolean = false
  def isDirectory: Boolean = true

  def isRoot: Boolean = parentPath.isEmpty

  def asDirectory: Directory = this
  def asFile: File = throw new FilesystemException("A directory cannot be converted to a file!")

  /**
    * Replace same name with new one(file or directory)
    *
    * @param entryName New entry name
    * @param newEntry New entry instance
    * @return The directory having a item which is replaced with same name
    */
  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  /**
    * search name in current directory
    *
    * @param entryName File or dir name to find in current dir
    * @return File or dir to find
    */
  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry =
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)

    findEntryHelper(entryName, contents)
  }

  /**
    * Add file or dir in current dir
    *
    * @param newEntry new file or dir
    * @return Current dir that added new file or dir
    */
  def addEntry(newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  /**
    * Remove file or dir in this dir
    * @param entryName name of file or dir to delete
    * @return Dir after deleting item
    */
  def removeEntry(entryName: String): Directory =
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(x => !x.name.equals(entryName)))

  /**
    * Find file or dir in current dir
    *
    * @param name File or dir to find
    * @return true: found
    */
  def hasEntry(name: String): Boolean =
    findEntry(name) != null

  /**
    * Get all folders' name on path
    * "/a/b/c/d" => ["a","b","c","d"]
    *
    * @return
    */
  def getAllFoldersInPath: List[String] = {
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => !x.isEmpty)
  }

  /**
    * Find Directory on the end dir on path
    *
    * @param path ex.[a,b,c,d] = "/a/b/c/d"
    * @return The end dir on path
    */
  def findDescendant(path: List[String]): Directory =
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)

  def findDescendant(relativePath: String): Directory =
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)
}

/**
  * Static Directory
  */
object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  /**
    * Create root dir
    *
    * @return root dir
    */
  def ROOT: Directory = Directory.empty("", "")

  /**
    * Create empty dir
    *
    * @param parentPath
    * @param name
    * @return Empty dir
    */
  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}
