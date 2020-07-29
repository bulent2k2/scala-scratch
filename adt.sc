/* Use in amm as:
 *  cd
 *  amm
 *  import $file.src.scala.adt
 *  adt.<TAB>
 */

/* 
 * product types:
 * like a struct in c, combines all the types, as if taking a cartesian product
 */
// Ornekler:
val ornek1 = (42, "Donald Duck")
final case class Kisi(no: Int, ad: String)
val ornek2 = Kisi(42, "Donald Duck")

/* sum types:
 * like a union in c, picks only one of the types, as if picking alternatives..
 * In Haskell, we do:
 *   data Either a b = Left a | Right b
 *   data Maybe a = Some a | Nothing
 * Later, we can search a name in a db, and return the person's data or a descriptive error message:
 *   type SearchResult = Either String UserData
 */
sealed trait Yada[So,Sa]
case class Sol[So,Sa](deger: So) extends Yada[So,Sa]
case class Sag[So,Sa](deger: Sa) extends Yada[So,Sa]
// Ornek:
type Mesaj = String
type Kisi2 = Yada[Mesaj, Kisi]
val ornek3: Kisi2 = Sol("Bulent yok")
val ornek4: Kisi2 = Sag(Kisi(8, "Baray Arman Basaran"))
