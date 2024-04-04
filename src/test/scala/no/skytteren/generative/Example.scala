package no.skytteren.generative

import verify.*

object Example extends BasicTestSuite:

  // Domain Models
  case class Person(
      firstName: String,
      surname: String,
      address: Address,
      phone: Option[String]
  )

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

  test("Domain model generator"):

    val p: Person = fromSeed(person.copy(firstName = "Genwin"))

    assert(p.firstName == "Genwin")

  test("Domain generate different models"):

    val (p1: Person, p2: Person) = fromSeed:
      (person, person)

    assert(p1 == p1)
    assert(p2 == p2)
    assert(p1 != p2)
