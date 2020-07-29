// https://typelevel.org/cats/jump_start_guide.html

// libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0-M1"
import $ivy.`org.typelevel::cats-core:2.0.0-M1`, cats._

// From: https://swsnr.de/blog/2018/02/24/algebraic-data-types-in-scala/#fnref:abstract

import cats.data._
import cats.Id
import cats.syntax.option._  // for .some. See: https://typelevel.org/cats/jump_start_guide.html

sealed trait Sum
case object A extends Sum

val res: EitherT[Id, Sum, Int] = for {
  id <- OptionT.pure[Id](42).toRight(A: Sum)
} yield id

// Now, do: test.A.some
