// See import $ivy section in: http://ammonite.io/#Ammonite-REPL
/* Use in amm as:
 *  cd; cd src/scala
 *  amm
 *  import $file..sless
 *  sless.<TAB>
 */

import $ivy.`com.chuusai::shapeless:2.3.3`, shapeless._

val ornek1 = (1 :: "lol" :: List(1, 2, 3) :: HNil)
