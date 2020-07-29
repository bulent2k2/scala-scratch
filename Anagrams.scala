package forcomp

// See for instructions and more comments:
//   ~/src/scala/scala-master/forcomp/src/main/scala/forcomp/Anagrams.scala
// or, ~/src/scala/course/archive/forcomp.zip

object Anagrams {
  type Word        = String
  type Sentence    = List[Word]
  type Occurrences = List[(Char, Int)]
  val dictionary: List[Word]                                     = loadDictionary  // see ./package.scala
  def wordOccurrences(w: Word): Occurrences                      = w.groupBy(_.toLower).map { case (k, v) => (k, v.size) }.toList.sorted
  def sentenceOccurrences(s: Sentence): Occurrences              = wordOccurrences(s.mkString)
  lazy val dictionaryByOccurrences: Map[Occurrences, List[Word]] = dictionary.groupBy(wordOccurrences(_)) withDefaultValue Nil
  def wordAnagrams(word: Word): List[Word]                       = dictionaryByOccurrences(wordOccurrences(word))
  def combinations(occurrences: Occurrences): List[Occurrences]  = {
    def singleCase(occ: (Char, Int)): List[Occurrences] = List()::(occ match {
      case (char, count) => (for (n <- 1 to count) yield List(char -> n)).toList
    })
    occurrences match {
      case Nil        => List(List())
      case head::Nil  => singleCase(head)
      case head::tail => for {
        elem <- singleCase(head)
        subset <- combinations(tail)
      } yield elem ++ subset
    }
  }
  def subtract(x: Occurrences, y: Occurrences): Occurrences =
    y.foldLeft(x.toMap){
      (map, pair) => pair match { // pair is element of the list (y) that is being folded over
        case (char, count) => {
          val now = map.apply(char)
          if (now == count) map - char else map updated (char, now - count)
        }
      }
    }.toList.sorted
  def sentenceAnagrams(sentence: Sentence): List[Sentence] = {
    def os2ana(os: Occurrences): List[Sentence] =
      os match {
        case Nil => List(Nil)
        case _ => for {
          comb <- combinations(os)
          word <- dictionaryByOccurrences(comb)
          sentence <- os2ana(subtract(os, comb))
        } yield word::sentence
      }
    os2ana(sentenceOccurrences(sentence))
  }
} // Anagrams in less than 50 lines!

object Main2 extends App {
  if (false) {

    // instead of using main, try auto-test! Or, try sbt > console >
    // and type anything here below:
    import forcomp.Anagrams._
    sentenceAnagrams(List("Evren"))
    val comma = ", "; val newline = "\n"
    /* 
    val myfamily = List("Baray", "Kayra") // Only 6! "Kay Ray Arab"
    val myfamily = List("Evren", "Kayra") // 300+ e.g., "Avery Karen" or "Ryan Eve ark"
    val myfamily = List("Baray", "Evren") // ~1.5k! "Barney Vera" "brave yearn"
    val myfamily = List("Baray", "and", "Kayra") // 1800 "Ankara yard bay",  "Dana Kay bar ray", "Ada Barry Kay an"
    val anas = sentenceAnagrams(myfamily)
     */
    val myfamily = List("Pinar", "Bulent") // ~25k! "Tulip Banner"
                                           // even after limiting to 2 or fewer words, we get 62 anagrams!
    val anas = sentenceAnagrams(myfamily).filter(_.size < 3).
      sortWith ( (xs,ys) => xs.head < ys.head )

    println(s"""$myfamily => ${anas mkString newline}""")

    {
      val s = List("Yes", "Man")
      val o = sentenceOccurrences(s)
      // val o = List('z' -> 1, 'a' -> 2, 'b' -> 1, 'c' -> 1)
        (o.toMap - 'a').updated('b', 3).toList.sorted
      val c = combinations(o)
    }

    val sen1: Sentence = List("I", "love", "you")
    val sen2: Sentence = List("Yes", "man")
    val ana2: List[Sentence] = List( List("en", "as", "my"), List("en", "my", "as"), List("man", "yes"), List("men", "say"), List("as", "en", "my"), List("as", "my", "en"), List("sane", "my"), List("Sean", "my"), List("my", "en", "as"), List("my", "as", "en"), List("my", "sane"), List("my", "Sean"), List("say", "men"), List("yes", "man") ).
      map(xs => xs.sorted).
      sortWith((a,b) => a.head < b.head)

    {
      val m = Map(1 -> 1, 2 -> 2, 3 -> 3)
      println(m take 2)
      lazy val md = dictionaryByOccurrences
      println(md.take(10))
      println(md(List('a' -> 1, 'e' -> 1, 't' -> 1)))
      println(wordAnagrams("team"))

      println(s"combo on one occ: ${combinations(List('z' -> 3)) mkString comma}")
      val l = List('a' -> 2, 'b' -> 2, 'z' -> 3)
      println(s"combo on two occ:\n${combinations(l) mkString newline}")
      val s2 = List('a' -> 2, 'b' -> 1, 'z' -> 2)
      val diff = subtract(l, s2)
      println(s"subtracting $s2 from $l, we get: $diff")
    }

    val t1 = List("You", "olive")
    println(s"$t1 -> ${combinations(sentenceOccurrences(t1)).take(10) mkString newline}")
    println(sentenceAnagrams(t1))

  }
}
