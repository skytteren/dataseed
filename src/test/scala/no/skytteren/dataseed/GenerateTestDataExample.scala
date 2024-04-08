package no.skytteren.dataseed

import verify.*

object GenerateTestDataExample extends BasicTestSuite:

  // Domain Models
  case class Person(
      firstName: String,
      surname: String,
      address: Address,
      phone: Option[String]
  ):
    val fullName = s"$firstName $surname"

  case class Address(
      stress: String,
      zipCode: ZipCode,
      area: String
  )

  case class ZipCode(value: Int) extends AnyVal

  // Domain Model Generators

  val address: Gen[Address] = Address(
    stress = string(between(2, 30)),
    zipCode = ZipCode(between(10000, 99999)),
    area = string(between(2, 30))
  )
  val person: Gen[Person] = Person(
    firstName = string(between(2, 30)),
    surname = string(between(2, 20)),
    address = address,
    phone = string(8).?
  )

  test("with copy"):

    val p: Person = fromSeed(person.copy(firstName = "Long John", surname = "Silver"))

    assert(p.fullName == "Long John Silver")

  test("new data"):
    val p: Person = fromSeed:
      Person(
        firstName = "Long John",
        surname = "Silver",
        address = Address(
          stress = string(between(2, 30)),
          zipCode = ZipCode(between(10000, 99999)),
          area = string(between(2, 30))
        ),
        phone = string(8).?
      )

    assert(p.fullName == "Long John Silver")

