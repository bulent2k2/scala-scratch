// A sample scala script to be used from ammonite (amm)

/* E.g., from scala source dir:
 *  cd ~/src/scala
 *  amm
 *  import $file.amm
 *  amm.<TAB>
 * 
 * Or, from home:
 *  cd 
 *  amm
 *  import $file.src.scala.amm
 *  amm.<TAB>
 */

// http://www.lihaoyi.com/post/StrategicScalaStylePrincipleofLeastPower.html
import scala.collection.mutable.Buffer
/* using it is easy:
 *   val foo: Buffer[Int] = Buffer.empty
 *   foo += 4 += 2 += 42
 * or more directly:
 *   val foo2 = Buffer(2,4,42)
 */

case class Item(id: Int, value: Int)
type Items = Buffer[Item]
class Player(var health: Int = 100, val items: Items = Buffer) {
  override def toString() = s"Player has health: $health and items: $items"
}

val player = new Player()
val p2 = new Player(200)

// adding an item is easy:
// amm.p2.items += amm.Item(200, 300)
