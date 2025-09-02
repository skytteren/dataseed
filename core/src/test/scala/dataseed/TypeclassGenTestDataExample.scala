package dataseed

import verify.*

object TypeclassGenTestDataExample extends BasicTestSuite:

  // Domain Models
  case class Person(
      firstName: String,
      surname: String,
      address: List[Address],
      phone: Option[String]
  ):
    val fullName = s"$firstName $surname"

  case class Address(
      street: String,
      zipCode: ZipCode,
      area: Area
  )

  case class ZipCode(value: Int) extends AnyVal
  case class Area(value: String)

  test("with copy"):

    val p: Person = Gen:
      given z: Gen[ZipCode] = ZipCode(between(10000, 99999))
      val p: Person = Gen[Person]
      p.copy(firstName = "Long John", surname = "Silver")

    assert(p.fullName == "Long John Silver")
    assert(p.address.length == 2)
