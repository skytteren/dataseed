# DataSeed
– is a little tool for generating test data for [scala](https://www.scala-lang.org).

![Scala CI](https://github.com/skytteren/dataseed/actions/workflows/scala.yml/badge.svg)

## Why?

Because relevant test data is too often lost in the clutter of all the setup code.

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
Distinguishing the important setup data from the need clutter is difficult at first glans. 
Only `firstName = "Long John"` and `surname = "Silver"` is important. 
The rest is just needed to be able to create `p: Person`.

## What? 

... does it do? 
It focuses tests on the important parts of the setup data.
The rest of the data is randomly generated stuff that nobody should care about.

### Example with good practice:

```scala 3
test("Full name"):
  // Given
  val p: Person = Gen: // generate
    Person(
      firstName = "Long John", // Important
      surname = "Silver", // Important
      address = Address( // Don't care
        street = string(between(2, 30)), // Just a random string
        zipCode = ZipCode(between(10000, 99999)), // A random number
        area = string(between(2, 30)) // Another random string
      ),
      phone = string(8).? // An optional random string 
    )

  //Then
  assert(p.fullName == "Long John Silver")
```

There is still a lot of setup. 
The difference is that only the important data is stated explicitly. 
Unimportant data is just randomly generated from a seed. 

### Example with splittig up the generated data

```scala 3

// generated address
val address: Gen[Address] = Address( 
  street = string(between(2, 30)), 
  zipCode = ZipCode(between(10000, 99999)), 
  area = string(between(2, 30)) 
)

val p: Person = Gen.fromSeed(seed = 12345): // generate
  Person(
    firstName = "Long John",
    surname = "Silver", 
    address = address, // using the generated address
    phone = string(8).?
  )

```

Seed can be set explicitly.

### Example where setting the seed

```scala 3
val p: Person = Gen.fromSeed(seed = 12345): // generate
  Person(
    firstName = "Long John", 
    surname = "Silver", 
    address = address, 
    phone = string(8).?
  )
```

## RELEASE

There is no release at the moment as all the important stuff is in 
[core/src/main/scala/dataseed/generate.scala]([core/src/main/scala/dataseed/generate.scala]).
It is less than 100 lines of code, and should stay that way.

So if you want to use it, copy the file and make your own adjustments.

The [names](names) subproject is just a playroom for generating names. 