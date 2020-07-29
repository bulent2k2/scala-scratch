// used in jnotebook: ~/src/scala/almond/Cheatsheet.ipynb
def isPrime(n: Int): Boolean = n match {
  case 2 => true
  case _ =>
    if (n <= 1 || n % 2 == 0) false
    else {
      var c = 3
      while (c <= math.sqrt(n).toInt + 1)
        if (n % c == 0) {
          return false
        } else {
          c = c + 2
        }
      true
    }
}
