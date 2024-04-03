package no.skytteren.generative

import no.skytteren.generative.MaxTries.given

object TestGen:
  @main
  def main(arg: String*): Unit =

    val r = fromSeed {
      val l = long.filter(_ > 1000).map(_.toInt).flatMap(_ => double).filter(_ > 10)
      val s = string(between(1, 100))
      val list = shuffle((0 to between(1, 1000)).toList).generate.filter(_ % 8 == 1).?
      s"$l - $s - $list - ${option(1)}"
    }
    println(r)
