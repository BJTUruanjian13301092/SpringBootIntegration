package com.example.spring_boot_test.scala

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val str:String = printMyString
    println(str)
    println("Scala : Hello world!")
  }

  def printMyString: String = {
    return "This is my printed String"
  }

}
