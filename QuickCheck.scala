package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    elem <- arbitrary[A]
    h <- oneOf(const(empty), genHeap)
  } yield insert(elem, h)
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  def min(x: A, y: A): A = if (x<y) x else y

  property("min of 2") = forAll { (a: Int, b: Int) =>
    val h = insert(b, insert(a, empty))
    findMin(h) == min(a,b)
  }

  property("add/delete") = forAll { a: Int =>
    isEmpty(deleteMin(insert(a, empty)))
  }

  property("add/add/delete") = forAll { (a: Int, b: Int) =>
    if (a == b) true
    else
      findMin(deleteMin(insert(b, insert(a, empty)))) != min(a, b)
  }

  property("re-insert min") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("meld two") = forAll { (h: H, h2: H) =>
    findMin(meld(h, h2)) == min(findMin(h), findMin(h2))
  }

  property("delete min") = forAll { (h: H) =>
    def deleteAllOccurrences(heap: H, min: A): H =
      if (isEmpty(heap) || findMin(heap) != min) heap
      else deleteAllOccurrences(deleteMin(heap), min)
    val m = if (isEmpty(h)) 0 else findMin(h)
    val heap = deleteAllOccurrences(insert(m, h), m)
    isEmpty(heap) || findMin(heap) != m
  }

  property("sort") = forAll { (h: H, a: Int) =>
    def loop(heap: H, acc: List[A]): List[A] =
      if (isEmpty(heap)) acc
      else {
        val m = findMin(heap)
        m :: loop(deleteMin(heap), acc)
      }
    // buggy deleteMin may be deleting more than the min!
    val sorted = loop(insert(a, h), Nil)
    // println(sorted)
    sorted == sorted.sorted && sorted.contains(a)
  }

/* no need:
  property("meld with empty") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    val heap = insert(m, h)
    findMin(meld(empty, heap)) == m
  }

  property("delete 2") = forAll { (h: H, a: Int) =>
    !isEmpty(deleteMin(insert(a, h)))
  }

  property("mix delete/insert") = forAll { a: Int => 
    val h = insert(3, insert(2, insert(1, deleteMin(insert(3, insert(2, insert(1, (insert(a, empty)))))))))
    val m1 = findMin(h)
    val m2 = findMin(deleteMin(h))
    val m3 = findMin(deleteMin(deleteMin(h)))
    val m4 = findMin(deleteMin(deleteMin(deleteMin(h))))
    if (a <= 1) m1 == a && m2 == 1 && m3 == 2 && m4 == 3
    else if (a > 3) m1 == 1 && m2 == 2 && m3 == 3 && m4 == a
  }
 */
}
