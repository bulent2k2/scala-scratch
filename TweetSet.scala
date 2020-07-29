package objsets

import TweetReader._

/**
  * A class to represent tweets.
  */
class Tweet(val user: String, val text: String, val retweets: Int) {
  override def toString: String =
    "User: " + user + "\n" +
  "Text: " + text + " [" + retweets + "]"
}

/**
  * This represents a set of objects of type `Tweet` in the form of a binary search
  * tree. Every branch in the tree has two children (two `TweetSet`s). There is an
  * invariant which always holds: for every branch `b`, all elements in the left
  * subtree are smaller than the tweet at `b`. The elements in the right subtree are
  * larger.
  *
  * Note that the above structure requires us to be able to compare two tweets (we
  * need to be able to say which of two tweets is larger, or if they are equal). In
  * this implementation, the equality / order of tweets is based on the tweet's text
  * (see `def incl`). Hence, a `TweetSet` could not contain two tweets with the same
  * text from different users.
  *
  *
  * The advantage of representing sets as binary search trees is that the elements
  * of the set can be found quickly. If you want to learn more you can take a look
  * at the Wikipedia page [1], but this is not necessary in order to solve this
  * assignment.
  *
  * [1] http://en.wikipedia.org/wiki/Binary_search_tree
  */

/*
 abstract class TweetSet {

 /**
 * This method takes a predicate and returns a subset of all the elements
 * in the original set for which the predicate is true.
 *
 * Question: Can we implment this method here, or should it remain abstract
 * and be implemented in the subclasses?
 */
 def filter(p: Tweet => Boolean): TweetSet = filterAcc(p, new Empty)
 
 /**
 * This is a helper method for `filter` that propagates the accumulated tweets.
 */
 def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet

 /**
 * Returns a new `TweetSet` that is the union of `TweetSet`s `this` and `that`.
 */
 def union(that: TweetSet): TweetSet
 
 /**
 * Returns the tweet from this set which has the greatest retweet count.
 *
 * Calling `mostRetweeted` on an empty set should throw an exception of
 * type `java.util.NoSuchElementException`.
 */
 def mostRetweeted: Tweet
 
 /**
 * Returns a list containing all tweets of this set, sorted by retweet count
 * in descending order. In other words, the head of the resulting list should
 * have the highest retweet count.
 *
 * Hint: the method `remove` on TweetSet will be very useful.
 */
 def descendingByRetweet: TweetList
 
 /**
 * Returns a new `TweetSet` which contains all elements of this set, and the
 * the new element `tweet` in case it does not already exist in this set.
 *
 * If `this.contains(tweet)`, the current set is returned.
 */
 def incl(tweet: Tweet): TweetSet

 /**
 * Returns a new `TweetSet` which excludes `tweet`.
 */
 def remove(tweet: Tweet): TweetSet

 /**
 * Tests if `tweet` exists in this `TweetSet`.
 */
 def contains(tweet: Tweet): Boolean

 /**
 * This method takes a function and applies it to every element in the set.
 */
 def foreach(f: Tweet => Unit): Unit
 }
 */

abstract class TweetSet {
  def empty: Boolean
  def toTweetList: TweetList
  def filter(p: Tweet => Boolean): TweetSet = filterAcc(p, new Empty)
  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet
  def union(that: TweetSet): TweetSet
  def mostRetweeted: Tweet
  def descendingByRetweet: TweetList
  def incl(tweet: Tweet): TweetSet
  def remove(tweet: Tweet): TweetSet
  def contains(tweet: Tweet): Boolean
  def foreach(f: Tweet => Unit): Unit
}

case class Empty() extends TweetSet {
  def empty = true
  def toTweetList: TweetList = Nil
  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = new Empty
  def union(that: TweetSet): TweetSet = that
  def contains(tweet: Tweet): Boolean = false
  def incl(tweet: Tweet): TweetSet = new NonEmpty(tweet, new Empty, new Empty)
  def remove(tweet: Tweet): TweetSet = this
  def foreach(f: Tweet => Unit): Unit = ()
  def mostRetweeted: Tweet = throw new java.util.NoSuchElementException("Asking for mostRetweeted from Empty set.")
  def descendingByRetweet: TweetList = Nil
}

case class NonEmpty(elem: Tweet, left: TweetSet, right: TweetSet) extends TweetSet {
  def empty = false
  def toTweetList: TweetList = new Cons(elem, left.toTweetList + right.toTweetList)
  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = {
    val cache = right.filterAcc(p, acc) union left.filterAcc(p, acc)
    if (p(elem)) cache.incl(elem) else cache
  }
  def union(that: TweetSet): TweetSet = (left union (right union that)) incl elem
  def contains(x: Tweet): Boolean =
    if (x.text < elem.text) left.contains(x)
    else if (elem.text < x.text) right.contains(x)
    else true
  def incl(x: Tweet): TweetSet = {
    if (x.text < elem.text) new NonEmpty(elem, left.incl(x), right)
    else if (elem.text < x.text) new NonEmpty(elem, left, right.incl(x))
    else this
  }
  def remove(tw: Tweet): TweetSet =
    if (tw.text < elem.text) new NonEmpty(elem, left.remove(tw), right)
    else if (elem.text < tw.text) new NonEmpty(elem, left, right.remove(tw))
    else left.union(right)
  def foreach(f: Tweet => Unit): Unit = {
    f(elem)
    left.foreach(f)
    right.foreach(f)
  }
  // This works, but is rather inefficient, it seems. Why? conversion to a list? BBX
  /*  def mostRetweeted: Tweet = {
   val f = (t: Tweet) => t.retweets > elem.retweets
   val subset = this.filter(f)
   if (subset.toTweetList == Nil) elem
   else subset.mostRetweeted
   }
   */
  // This also works, but, uses toString
  /*
   def mostRetweeted: Tweet = {
   def loop(maxSoFar: Tweet, set: TweetSet): Tweet = { // set contains maxSoFar
   def f: (Tweet) => Boolean = (t: Tweet) => t.retweets > maxSoFar.retweets
   val ts = set.filter(f)
   if (ts.toString == ".") maxSoFar else loop(ts.mostRetweeted, ts)
   }
   loop(elem, this)
   }
   */
  // needed to add empty method. Any other way?? BBX. Yes :-) Use case classes..
  def mostRetweeted: Tweet = {
    def loop(maxSoFar: Tweet, biggerSet: TweetSet): Tweet = { // set contains maxSoFar
      val smallerSet = biggerSet.filter(_.retweets > maxSoFar.retweets)
      smallerSet match {
        case Empty() => maxSoFar
        case NonEmpty(_,_,_) => loop(smallerSet.mostRetweeted, smallerSet)
      }
      //      if (smallerSet.empty) maxSoFar else
      //        loop(smallerSet.mostRetweeted, smallerSet)
    }
    loop(elem, this)
  }

  def descendingByRetweet: TweetList = {
    val most = mostRetweeted
    new Cons(most, remove(most).descendingByRetweet)
  }
}

trait TweetList {
  override def toString: String
  def head: Tweet
  def tail: TweetList
  def isEmpty: Boolean
  def foreach(f: Tweet => Unit): Unit =
    if (!isEmpty) {
      f(head)
      tail.foreach(f)
    }
  def +(that: TweetList): TweetList
  def take(n: Int): TweetList
}

object Nil extends TweetList {
  override def toString: String = "."
  def head = throw new java.util.NoSuchElementException("head of EmptyList")
  def tail = throw new java.util.NoSuchElementException("tail of EmptyList")
  def isEmpty = true
  def +(that: TweetList): TweetList = that
  def take(n: Int): TweetList = this
}

class Cons(val head: Tweet, val tail: TweetList) extends TweetList {
  def isEmpty = false
  override def toString: String = head.retweets + "," + tail.toString()
  def +(that: TweetList): TweetList = new Cons(head, tail + that)
  def take(n: Int): TweetList = if (n > 0) new Cons(head, tail.take(n-1)) else Nil
}


object GoogleVsApple {
  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
  val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

  val all = TweetReader.allTweets
  def findUnion(tags: List[String], set: TweetSet): TweetSet = {
    if (tags.isEmpty) set
    else findUnion(tags.tail, set.union(all.filter(t=>t.text.contains(tags.head))))
  }
  lazy val googleTweets: TweetSet = findUnion(google, new Empty)
  lazy val appleTweets: TweetSet = findUnion(apple, new Empty)
  /**
    * A list of all tweets mentioning a keyword from either apple or google,
    * sorted by the number of retweets.
    */
  lazy val trending: TweetList = googleTweets.union(appleTweets).descendingByRetweet
}

object Main extends App {
  // Print the trending tweets
  println("Google tweets top 3")
  println(GoogleVsApple.googleTweets.toTweetList take 3)
  println("trending top 4")
  GoogleVsApple.trending.take(4).foreach(println)
}
