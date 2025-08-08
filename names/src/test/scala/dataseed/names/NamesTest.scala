package dataseed.names

import dataseed.Gen
import verify.*

object NamesTest extends BasicTestSuite:
  test("gen femaleName from seed"):
    val name = Gen:
      femaleName
    assert(name == "Ida")

  test("gen maleName from seed"):
    val name = Gen:
      maleName
    assert(name == "Felix")

  test("gen probablyFemaleName from seed"):
    val name = Gen:
      probablyFemaleName
    assert(name == "Ingrid")

  test("gen probablyMaleName from seed"):
    val name = Gen:
      probablyMaleName
    assert(name == "Vetle")


