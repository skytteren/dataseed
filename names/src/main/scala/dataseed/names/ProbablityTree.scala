package dataseed
package names

import scala.math.Numeric.Implicits.infixNumericOps

enum ProbablityTree[T]:
  import ProbablityTree.Weight
  def weight: Weight
  private case Leaf(value: T, weight: ProbablityTree.Weight)
  private case Branch(left: ProbablityTree[T], right: ProbablityTree[T], weight: ProbablityTree.Weight)

  def append(value: T, weight: Weight): ProbablityTree[T] =
    if weight == 0L then throw IllegalArgumentException(s"wheight $weight no suppoertet on value: $value")
    else
      this match
        case l @ Leaf(_, w) =>
          Branch(left = l, right = Leaf(value, weight), weight = weight + w)
        case Branch(l, r, w) if l.weight < r.weight =>
          Branch(l.append(value, weight), r, w + weight)
        case Branch(l, r, w) =>
          Branch(l, r.append(value, weight), w + weight)

  def apply(index: Long): T = this match
    case Leaf(v, _)                          => v
    case Branch(l, _, _) if index < l.weight => l(index)
    case Branch(l, r, _)                     => r(index - l.weight)

  def gen: Gen[T] =
    val index = between(0, weight)
    apply(index)

  def size: Int = this match
    case Leaf(_, _)      => 1
    case Branch(l, r, _) => l.size + r.size

  def maxHeight: Int = this match
    case Leaf(_, _)      => 1
    case Branch(l, r, _) => Math.max(l.maxHeight, r.maxHeight) + 1

  def minHeight: Int = this match
    case Leaf(_, _)      => 1
    case Branch(l, r, _) => Math.min(l.minHeight, r.minHeight) + 1

  private def pretty(indent: Int, spacer: String): String = this match
    case Leaf(value, weight) => spacer * indent + s"$value($weight)"
    case Branch(left, right, _) =>
      spacer * indent + "(\n" +
        left.pretty(indent + 1, spacer) + ",\n" +
        right.pretty(indent + 1, spacer) + "\n" +
        spacer * indent + ")"
  def pretty: String = pretty(0, "  ")

  def stats: String =
    s"""Tree:
       |  size:         ${this.size}
       |  max height:   ${this.maxHeight}
       |  min height:   ${this.minHeight}
       |  weight:       ${this.weight}
       |""".stripMargin

object ProbablityTree:
  type Weight = Long
  def apply[T](value: T, weight: Weight): ProbablityTree[T] =
    if weight <= 0L
    then throw IllegalArgumentException(s"wheight $weight no suppoertet on value: $value")
    else Leaf(value, weight)

  def fromTuple[T, W: Numeric](head: (T, W), rest: (T, W)*): ProbablityTree[T] =
    rest.foldLeft(ProbablityTree[T](head._1, head._2.toLong)):
      case (a, (t, w)) => a.append(t, w.toLong)

  def fromSeq[T, W: Numeric](list: Seq[(T, Weight)]): ProbablityTree[T] =
    fromTuple(list.head, list.tail*)
