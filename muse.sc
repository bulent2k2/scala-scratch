// do: 
// scala
// :lo /users/bulentbasaran/src/scala/muse.sc

//  https://swsnr.de/blog/2018/02/24/algebraic-data-types-in-scala/

/*
 * product types (generalization of pairs,tuples)
 */

final case class User(id: Int, name: String)
val user=User(42, "Donald Duck")
val u2  =User(2, "U2")

/* sum types - remember haskell? 
 *   data Either a b = Left a | Right b
 */

// Note! These are already defined in Scala!
sealed trait Either[L,R]
case class Left [L,R](value: L) extends Either[L,R]
case class Right[L,R](value: R) extends Either[L,R]

