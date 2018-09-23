package com.rtvm.scala.oop.files

/**
  * File or Directory
  *
  * @param parentPath Absolute path of parent
  * @param name File or Dir name
  */
abstract class DirEntry(val parentPath: String, val name: String) {

  /**
    * Absolute path for current file or dir
    *
    * @return
    */
  def path: String = {
    val separatorIfNecessary = {
      if (Directory.ROOT_PATH.equals(parentPath)) ""
      else Directory.SEPARATOR
    }

    parentPath + separatorIfNecessary + name
  }

  def asDirectory: Directory
  def asFile: File

  def isDirectory: Boolean
  def isFile: Boolean
  def getType: String
}
