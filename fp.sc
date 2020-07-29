// monad is a type that supports map/flatMap/filter .. It has many uses..
// practice with flatMap:

val l = (0 until 5).toList
val l2 = l flatMap { x => List(x, x*x, x*x*x) }
val l3 = l2.distinct.sorted

