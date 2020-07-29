// See: ~/src/haskell/stack/shapes/src/Shape.hs
import math.{abs, sqrt}

sealed abstract trait Shape {
  def area()  : Long = 0
  def length(): Long = 0
  def accurate_area()  : Double = 0.0
  def accurate_length(): Double = 0.0
}
case class Point(x: Long, y: Long) extends Shape {
  override def toString() = "(" + x + ", " + y + ")"
  def aligned(that: Point) = this.x == that.x || this.y == that.y
}
case class Edge(p1: Point, p2: Point) extends Shape {
  require(p1 != p2)
  override def toString() = s"E($p1, $p2)"
  val dx = abs(p1.x - p2.x)
  val dy = abs(p1.y - p2.y)
  override def accurate_length() = sqrt(dx * dx + dy * dy)
  override def length() = if (p1 aligned p2) dx + dy else accurate_length.round.toLong
}
// TODO: equality check!
case class Triangle(p1: Point, p2: Point, p3: Point) extends Shape {
  override def toString() = s"<$p1, $p2, $p3>"
  def edges = List(Edge(p1, p2), Edge(p2, p3), Edge(p3, p1))
  def edge_lengths = edges map (_.accurate_length)
  override def accurate_length = edge_lengths.sum
  override def length = accurate_length.toLong
  override def accurate_area = {
    edge_lengths match {
      case List(a, b, c) => 
        val hp: Double = (a + b + c) / 2.0
        sqrt(hp * (hp - a) * (hp - b) * (hp - c))
    }
  }
  override def area = accurate_area.toLong
}
case class Rectangle(p1: Point, p2: Point) extends Shape {
  require(!(p1 aligned p2))
  val (llx, urx) = if (p1.x < p2.x) (p1.x, p2.x) else (p2.x, p1.x)
  val (lly, ury) = if (p1.y < p2.y) (p1.y, p2.y) else (p2.y, p1.y)
  override def toString() = s"[$p1, $p2]"
  def slice(from_UL_to_LR: Boolean): (Triangle, Triangle) = {
    val (ll, ul) = (Point(llx, lly), Point(llx, ury))
    val (ur, lr) = (Point(urx, ury), Point(urx, lly))
    if (from_UL_to_LR) // backhand slice (for a righty)
      (Triangle(ul, ll, lr), Triangle(lr, ur, ul))
    else // forehand slice
      (Triangle(ur, lr, ll), Triangle(ll, ul, ur))
  }
  override def area = (urx - llx) * (ury - lly)
  override def accurate_area = area.toDouble
}

object Test { // See: ./TEST
  val (p1,p2,p3,p4,p5) = (Point(0, 0), Point(4, 0), Point(4, 3), Point(10, 0), Point(0, 10))
  val e1 = Edge(p4, p5)
  val tri1 = Triangle(p1, p2, p3)
  val tri2 = Triangle(p1, p4, p5)
  val tri3 = Triangle(p1, p5, p4)
  assert(tri2 != tri3) // TODO: FIX THIS!
  val rect1 = Rectangle(p1, p3)
  List(p3, e1, tri1, tri2, rect1) foreach (s => 
    println(s"$s has:\n\tlength ${(s.length, s.accurate_length)}\n\tarea ${(s.area, s.accurate_area)}"))
}
Test

