// l for loop..

import scala.annotation.tailrec

type Num = BigInt

def sum(f: Num => Num, a: Num, b: Num): Num = {
  @tailrec
  def loop(x: Num, acc: Num): Num = if (x > b) acc else loop(x+1, acc + f(x))
  loop(a, 0)
}

val ex1 = ("Sum 1 to 10", sum(x => x, 1, 10))
val ex2 = ("Sum 1 to 100", sum(x => x, 1, 100))
val ex3 = ("1 to 5 squared summed", sum(x => x*x, 1, 5))

def factorial(n: Num): Num = {
  @tailrec
  def loop(x: Num, prod: Num): Num = if (x == 1) prod else loop(x-1, prod * x)
  loop(n, 1)
}

val ex4 = (
  (x: Int) => 
  (s"1 to $x summed after factorial", sum(y => factorial(y), 1, x))
)(5)

val ex5 = for( x <- 1 to 10) yield(s"1 to  $x summed after factorial", sum(factorial, 1, x))

println("see example values: loop.ex<n>")
