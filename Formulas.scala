package org.stairwaybook.scells

// Recursion: Formula is a tree of Formulas!

// Note: formulas contain standard functions like add/sum and custom functions can be added using mixin composition (with keyword?)

trait Formula

case class Coord(row: Int, column: Int) extends Formula {
  override def toString = ('A' + column).toChar.toString + row
}
case class Range(c1: Coord, c2: Coord) extends Formula {
  override def toString = c1.toString + ":" + c2.toString
}
case class Number(value: Double) extends Formula {
  override def toString = value.toString
}
case class Textual(value: String) extends Formula {
  override def toString = value
}
case class Application(function: String, arguments: List[Formula]) extends Formula {
  override def toString = function + arguments.mkString("(", ",", ")")
}
object Empty extends Textual("")
