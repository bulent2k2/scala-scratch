/* 
 cd /tmp/
 sbt
 console
 */

val s = "Hello"
s.length
s.foreach(println)
s.reverse.foreach(print)
s.getBytes
s.filter(_ != 'l')
"scala".drop(2).take(2).capitalize

val s1 = "foo"
val s2 = "foo"
val s3: String = null
s1 == s2
s1 == s3
s3 == s3
s2.toUpperCase

val s4 = "Foo"
s1 == s4
s1.equalsIgnoreCase(s4)

