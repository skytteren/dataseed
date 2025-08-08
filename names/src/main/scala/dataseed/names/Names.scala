package dataseed
package names

type Count = Int

import scala.util.Using

case class Name(value: String, count: Int)

private def readNames(resource: String): List[Name] =
  Using(io.Source.fromResource(resource = resource)):
    _.getLines().toList
      .map: s =>
        s.split(';') match
          case Array(name, count) =>
            Name(name, count.toInt)
  .get

val femaleNames: List[Name] = readNames("female.csv")
val maleNames: List[Name] = readNames("male.csv")

def probableSelection(names: List[Name]): ProbablityTree[String] =
  ProbablityTree.fromSeq[String, Int](names.map(n => n.value -> n.count))

lazy val probablyFemaleName: Gen[String] =
  probableSelection(femaleNames).gen

lazy val probablyMaleName: Gen[String] =
  probableSelection(maleNames).gen

def femaleName: Gen[String] =
  femaleNames(between(0, femaleNames.size))._1

def maleName: Gen[String] =
  maleNames(between(0, maleNames.size))._1
