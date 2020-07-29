package streams

// BBX: Test in sbt as:
// console
// import streams._
// val i = Bloxorz.InfiniteLevel
// i.startBlock
// i.goal
// The following didn't halt until I fixed from in Solver.scala to use #::: instead of ++
// i.pathsFromStart

/**
 * A main object that can be used to execute the Bloxorz solver
 */
object Bloxorz extends App {

//  BBX: gives me: java.lang.OutOfMemoryError: GC overhead limit exceeded
//  Not lazy enough??

  /**
   * A level constructed using the `InfiniteTerrain` trait which defines
   * the terrain to be valid at every position.
   */
  object InfiniteLevel extends Solver with InfiniteTerrain {
    val startPos = Pos(1,3)
    val goal = Pos(5,8)
  }

  val i = InfiniteLevel
  println(s"Infinite terrain start position: ${i.startPos}, target: ${i.goal}")
  println(s"Solution: ${InfiniteLevel.solution} -> ${InfiniteLevel.show}")

  /**
   * A simple level constructed using the StringParserTerrain
   */
  abstract class Level extends Solver with StringParserTerrain

/* solutions:

0: List(Down, Right, Up)
1: List(Right, Right, Down, Right, Right, Right, Down)
6: List(Right, Right, Right, Down, Right, Down, Down, Right, Down, Down, Right, Up, Left, Left, Left, Up, Up, Left, Up, Up, Up, Right, Right, Right, Down, Down, Left, Up, Right, Right, Down, Right, Down, Down, Right)

 */

  object Level0 extends Level {
    val level =
      """------
        |--ST--
        |--oo--
        |--oo--
        |------""".stripMargin
  }

  println(s"Level0: ${Level0.show}")

  /**
   * Level 1 of the official Bloxorz game
   */
  object Level1 extends Level {
    val level =
      """ooo-------
        |oSoooo----
        |ooooooooo-
        |-ooooooooo
        |-----ooToo
        |------ooo-""".stripMargin
  }

  println(s"Level1: ${Level1.show}")

  // passcode: 524383
  object Stage6 extends Level {
    val level =
      """-----oooooo----
        |-----o--ooo----
        |-----o--ooooo--
        |Sooooo-----oooo
        |----ooo----ooTo
        |----ooo-----ooo
        |------o--oo----
        |------ooooo----
        |------ooooo----
        |-------ooo-----""".stripMargin
  }

  println(s"Level6: ${Stage6.show}")

}
