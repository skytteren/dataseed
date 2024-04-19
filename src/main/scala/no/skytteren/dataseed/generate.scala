package no.skytteren.dataseed

import scala.annotation.targetName
import scala.collection.BuildFrom
import scala.util.Random

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

  def generate(using r: Random): T = gen

  @targetName("optional")
  def ? : Gen[Option[T]] = r ?=> Option.when(boolean)(gen)

end extension

object Gen:
  def apply[T](f: Gen[T], seed: Seed = 1): T = f.apply(using Random(seed))
  def fromSeed[T](seed: Seed)(f: Gen[T]): T = f.apply(using Random(seed))

def apply[T](f: Gen[T]): Gen[T] = f

def option[T](f: Gen[T]): Gen[Option[T]] = Option.when(boolean)(f)

def long: Gen[Long] = r ?=> if boolean then r.nextLong() else -r.nextLong()
def int: Gen[Int] = r ?=> if boolean then r.nextInt() else -r.nextInt()
def boolean: Gen[Boolean] = r ?=> r.nextBoolean()
def double: Gen[Double] = r ?=> r.nextDouble() * (if boolean then Double.MaxValue else Double.MinValue)
def uniformDistribution: Gen[Double] = r ?=> r.nextDouble()
def gaussianDistribution: Gen[Double] = r ?=> r.nextGaussian()
def float: Gen[Float] = r ?=> r.nextFloat() * (if boolean then Float.MaxValue else Float.MinValue)
def between(minInclusive: Double, maxExclusive: Double): Gen[Double] = r ?=> r.between(minInclusive, maxExclusive)
def between(minInclusive: Int, maxExclusive: Int): Gen[Int] = r ?=> r.between(minInclusive, maxExclusive)
def between(minInclusive: Long, maxExclusive: Long): Gen[Long] = r ?=> r.between(minInclusive, maxExclusive)
def char: Gen[Char] = r ?=> r.nextPrintableChar()
def string(length: Int): Gen[String] = r ?=> (0 to length).map(_ => char).mkString
def alphanumericString(length: Int): Gen[String] = r ?=> r.alphanumeric.take(length).mkString
def shuffle[T, C](xs: IterableOnce[T])(implicit bf: BuildFrom[xs.type, T, C]): Gen[C] = r ?=> r.shuffle(xs)
