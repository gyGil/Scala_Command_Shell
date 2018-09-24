package com.rtvm.scala.oop.files

import com.rtvm.scala.oop.filesystem.FilesystemException

/**
  * File
  *
  * @param parentPath Absolute path
  * @param name File name
  * @param conents Text in file
  */
class File(override  val parentPath: String, override val name: String, val contents: String)
  extends DirEntry (parentPath, name){

  def getType: String = "File"

  def isFile: Boolean = true
  def isDirectory: Boolean = false

  def asFile: File = this
  def asDirectory: Directory =
    throw new FilesystemException("A file cannot be converted to a directory!")

  def setContents(newContents: String): File =
    new File(parentPath, name, newContents)

  def appendContents(newContents: String): File =
    setContents(contents + "/n" + newContents)
}

/**
  * Static File
  */
object File {

  /**
    * Create empty file
    *
    * @param parentPath
    * @param name
    * @return
    */
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
