package no.skytteren.dataseed

import verify._

object GenTest extends BasicTestSuite:
  test("gen int from seed"):
    val i = fromSeed(int)
    assert(i == 431529176)

  test("gen long from seed"):
    val l = fromSeed(long)
    assert(l == 1853403699951111791L)

  test("gen boolean from seed"):
    val b = fromSeed(boolean)
    assert(b)

  test("gen double from seed"):
    val d = fromSeed(double)
    assert(d == -1.3138947058478963e308)

  test("gen float from seed"):
    val f = fromSeed(float)
    assert(f == -2.4870493e38f)

  test("gen char from seed"):
    val c = fromSeed(
      char,
      seed = 10
    )
    assert(c == 'z')

  test("gen string from seed"):
    val s = fromSeed:
      string(64)
    assert(s == """\{<nG-!yy]fLBf=7I]U85S}2k$IVJc|]Sqz.De83.E&vz'Om!rw>92;2_Z'e-45pM""")

  test("gen alphanumericString from seed"):
    val s = fromSeed:
      alphanumericString(64)
    assert(s == """NAvZuGESoIJ7hbqOIsAV4iWta9qh1yp4iuhRxkraBq7ZFYeOIN8pKbyLI3gOYbIv""")

  test("shuffle"):
    val l = fromSeed:
      shuffle((1 to 10).toList)
    assert(l == List(7, 10, 8, 9, 5, 3, 1, 4, 2, 6))

  test("option"):
    val o: Option[Int] = fromSeed:
      option(1)
    assert(o.contains(1))

  test("option 2"):
    val o: Option[Int] = fromSeed(
      option(1),
      seed = 4096
    )
    assert(o.isEmpty)

  test("?"):
    val o: Option[Int] = fromSeed:
      1.?
    assert(o.contains(1))

  test("? 2"):
    val o = fromSeed(
      1.?,
      seed = 4096
    )
    assert(o.isEmpty)

  test("gen filter from seed"):
    assert(fromSeed(long) == 1853403699951111791L)
    assert(fromSeed(long) == 1853403699951111791L)

    val l2 = fromSeed(long.filter(_ < 0L))
    assert(l2 == -3831662765844904176L)

  test("gen map from seed"):
    val i = fromSeed(double.map(_.toInt))
    assert(i == -2147483648)

  test("gen flatMap from seed"):
    val i = fromSeed(double.flatMap(_ => int))
    assert(i == -892128508)

  test("gen generate from seed"):
    val i: Int = fromSeed:
      int.generate.map(_ => 3)
    assert(i == 3)

  test("gen generate from seed"):
    val i: Int = fromSeed:
      int.map(_ => 3)
    assert(i == 3)

  test("gen between for int"):
    val i: Int = fromSeed:
      between(1, 100)
    assert(i == 16)

  test("gen between for double"):
    val i: Double = fromSeed:
      between(1d, 100d)

    assert(i == 73.3569408796258d)

  test("gen between for long"):
    val i: Long = fromSeed:
      between(1L, 100L)
    assert(i == 16L)
