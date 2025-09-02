package dataseed

import scala.annotation.targetName
import scala.collection.BuildFrom
import scala.util.Random
import scala.compiletime.*
import scala.deriving.Mirror
import scala.reflect.ClassTag

type Seed = Long

type Gen[T] = Random ?=> T

opaque type MaxTries = Int
object MaxTries:
  def apply(number: Int): MaxTries = number
  extension (max: MaxTries)
    @targetName("lt")
    def >(value: Int): Boolean = max < value

given default: MaxTries = 1000

extension [T](gen: Gen[T])
  def filter(predicate: T => Boolean)(using maxTries: MaxTries): Gen[T] = r ?=>
    var i = 0
    var value = gen.apply(using r)
    while maxTries > i do
      if predicate.apply(value) then return value
      else
        value = gen.apply(using r)
        i += 1

    throw new IllegalStateException("Too many tries")
  end filter

  def map[B](f: T => B): Gen[B] = r ?=> f(gen.apply(using r))

  def flatMap[B](f: T => Gen[B]): Gen[B] = r ?=> f(gen.apply(using r))

  def generate(using Random): T = gen.apply

  @targetName("optional")
  def ? : Gen[Option[T]] = r ?=> Option.when(boolean)(gen)

end extension

object Gen:
  def apply[T](f: Gen[T], seed: Seed = 1): T = f.apply(using Random(seed))
  def fromSeed[T](seed: Seed)(f: Gen[T]): T = f.apply(using Random(seed))

def apply[T](f: Gen[T]): Gen[T] = f

def option[T](f: Gen[T]): Gen[Option[T]] = Option.when(boolean)(f)

given long: Gen[Long] = r ?=> if boolean then r.nextLong() else -r.nextLong()
given int: Gen[Int] = r ?=> if boolean then r.nextInt() else -r.nextInt()
given boolean: Gen[Boolean] = r ?=> r.nextBoolean()
given double: Gen[Double] = r ?=> r.nextDouble() * (if boolean then Double.MaxValue else Double.MinValue)
given short: Gen[Short] = r ?=> int.toShort
given float: Gen[Float] = r ?=> r.nextFloat() * (if boolean then Float.MaxValue else Float.MinValue)
given char: Gen[Char] = r ?=> r.nextPrintableChar()

val uniformDistribution: Gen[Double] = r ?=> r.nextDouble()
val gaussianDistribution: Gen[Double] = r ?=> r.nextGaussian()
def between(minInclusive: Double, maxExclusive: Double): Gen[Double] = r ?=> r.between(minInclusive, maxExclusive)
def between(minInclusive: Int, maxExclusive: Int): Gen[Int] = r ?=> r.between(minInclusive, maxExclusive)
def between(minInclusive: Long, maxExclusive: Long): Gen[Long] = r ?=> r.between(minInclusive, maxExclusive)
def string(length: Int): Gen[String] = r ?=> (0 to length).map(_ => char).mkString
def alphanumericString(length: Int): Gen[String] = r ?=> r.alphanumeric.take(length).mkString
def shuffle[T, C](xs: IterableOnce[T])(using BuildFrom[xs.type, T, C]): Gen[C] = r ?=> r.shuffle(xs)
def oneOf[A](xs: Seq[A]): Gen[A] = xs(between(0, xs.size))
def listOf[A](gen: Gen[A], min: Int = 0, max: Int = 5): Gen[List[A]] = List.fill(between(min, max))(gen)

trait DerivedGen[T]:
  def apply(using random: Random): T

object DerivedGen:
  def derived[T: DerivedGen as gen]: DerivedGen[T] = gen
  def apply[T: DerivedGen as gen]: T = Gen.apply(gen.apply)

inline given derivedProduct[T <: Product](using m: Mirror.ProductOf[T]): DerivedGen[T] = new DerivedGen[T]:
  def apply(using r: Random): T = m.fromTuple(genTuple(summonAll[Tuple.Map[m.MirroredElemTypes, DerivedGen]])(using r))

private def genTuple[T <: Tuple](gens: Tuple.Map[T, DerivedGen]): DerivedGen[T] = r ?=>
  val gensList = gens.toList.asInstanceOf[List[DerivedGen[Any]]]
  val list = gensList.map[Any](g => g(using r))
  Tuple.fromArray(list.toArray).asInstanceOf[T]

inline given derivedSum[T](using m: Mirror.SumOf[T]): DerivedGen[T] = new DerivedGen[T]:
  def apply(using r: Random): T = (oneOf(summonAll[Tuple.Map[m.MirroredElemTypes, DerivedGen]].toList)(using r)).asInstanceOf[T]

given [T: Gen as gen]: DerivedGen[T] = r ?=> gen

given str: Gen[String] = r ?=> alphanumericString(between(1, 1000))

given [T: DerivedGen as gen]: DerivedGen[Option[T]] = r ?=> option(gen(using r))
given [T: DerivedGen as gen]: DerivedGen[List[T]] = r ?=> listOf(gen(using r))
