# DataSeed
– is a little tool for generating test data for [scala](https://www.scala-lang.org).

![Scala CI](https://github.com/skytteren/dataseed/actions/workflows/scala.yml/badge.svg)

## Why?

Relevant test data is too often lost in the clutter.

When explicitly stated data is used for testing it difficult to see what is important for a given scenario.

### Example of bad practice:

```scala 3
test("Full name"):
  // Given clutter
  val p: Person = Person(
    firstName = "Long John",
    surname = "Silver",
    address = Address(
      street = "My street",
      zipCode = ZipCode(12345),
      area = "Tiny Town"
    ),
    phone = "555 9999"
  )

  //Then
  assert(p.fullName == "Long John Silver")
```

There is a lot of setup data. 
To distinguish the important setup data from the need clutter is difficult to see by first glans. 
`firstName = "Long John"` and `surname = "Silver"` is important. 
The rest is just needed to be able to create `p: Person`.

## What? 

### Example with good practice:

```scala 3
test("Full name"):
  // Given
  val p: Person = Gen:
    Person(
      firstName = "Long John",
      surname = "Silver",
      address = Address(
        street = string(between(2, 30)),
        zipCode = ZipCode(between(10000, 99999)),
        area = string(between(2, 30))
      ),
      phone = string(8).?
    )

  //Then
  assert(p.fullName == "Long John Silver")
```

There is still a lot of setup. 
The difference is that only the important data is stated explicitly. 
Unimportant data is just randomly generated from a seed. 
