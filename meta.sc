// https://scalameta.org/docs/trees/guide.html

/* In amm, do:
 *   import $file.meta
 *   meta.me.exampleTree
 *   show(res1)
 */

import $ivy.`org.scalameta::scalameta:4.1.0`, scala.meta._

object me {
  val path = java.nio.file.Paths.get("/", "Users", "bulentbasaran", "src", "scala", "meta.sc")
  val bytes = java.nio.file.Files.readAllBytes(path)
  val text = new String(bytes, "UTF-8")
  val input = Input.VirtualFile(path.toString, text)
  val exampleTree = input.parse[Source].get
  println(s"Here I am: $exampleTree")
}
