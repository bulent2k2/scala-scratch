package org.stairwaybook.scells

import scala.util.parsing.combinator._

object FormulaParsers extends RegexParsers {

  def ident:   Parser[String] = """[a-zA-Z_]\w*""".r
  def decimal: Parser[String] = """-?\d+(\.\d*)?""".r
  // TODO: support multiple letters (for rows more than Z..)
  def cell:    Parser[Coord]  = """[a-zA-Z]\d+""".r ^^ { s =>
    val column = s.charAt(0).toUpper - 'A'
    val row    = s.substring(1).toInt
    Coord(row, column)
  }
  def range:   Parser[Range]  = 
    cell~":"~cell ^^ {
      case c1~":"~c2 => Range(c1, c2)
    }
  def number:  Parser[Number] =
    decimal ^^ (d => Number(d.toDouble))
  def application: Parser[Application] =
    ident~"("~repsep(expr, ",")~")" ^^ {
      case f~"("~ps~")" => Application(f, ps)
    }
  // note: range should not be a top level formula
  def expr:    Parser[Formula] =
    range | cell | number | application
  def textual: Parser[Textual] =
    """[^=].*""".r ^^ Textual
  def formula: Parser[Formula] = 
    number | textual | "="~>expr

  // ! The grammer is now fully defined!
  // Now, we just parse an input string according to this grammer
  // to convert it into a tree of formulas:

  def parse(input: String): Formula = 
    parseAll(formula, input) match {
      case Success(e, _) => e
      case f: NoSuccess => Textual("[" + f.msg + "]")
    }

}

/* 
% fsc FormulaParsers.scala 
/Users/bulentbasaran/src/scala/f2c/src/FormulaParsers.scala:22: warning: match may not be exhaustive.
It would fail on the following inputs: ~(_, ")"), ~(_, (x: String forSome x not in ")"))
    ident~"("~repsep(expr, ",")~")" ^^ {
                                       ^
one warning found
*/
