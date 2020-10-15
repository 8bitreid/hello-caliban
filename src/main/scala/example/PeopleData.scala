package example

import caliban.schema.Annotations.{GQLDeprecated, GQLDescription}
import example.PeopleData.Relationship.{CHILD, FRIEND, PARENT}

object PeopleData {
  @GQLDescription("usually a human...")
  case class Person(
    id: Int,
    @GQLDescription("a name for being social.")
    name: Name,
    age: Option[Int] = None,
    relationship: Option[Relationship] = None
  )

  case class Name(first: String, last: String)

  sealed trait Relationship

  object Relationship {
    case object PARENT extends Relationship
    case object CHILD extends Relationship
    case object FRIEND extends Relationship
  }

  private val Reid = Person(
    id = 1,
    name = Name("Reid", "Mewborne"),
    age = Some(32),
    relationship = Some(PARENT)
  )

  private val Lauren = Person(
    id = 2,
    name = Name("Lauren", "Mewborne"),
    age = None,
    relationship = Some(PARENT)
  )

  private val Brooklyn = Person(
    id = 3,
    name = Name("Brooklyn", "Mewborne"),
    age = Some(3),
    relationship = Some(CHILD)
  )

  private val Levi = Person(
    id = 4,
    name = Name("Levi", "Mewborne"),
    age = Some(1),
    relationship = Some(CHILD)
  )

  private val Matt = Person(
    id = 5,
    name = Name("Matt", "W"),
    age = None,
    relationship = Some(FRIEND)
  )

  private val Bennett = Person(
    id = 6,
    name = Name("Richard", "Bennett"),
    age = None,
    relationship = Some(FRIEND)
  )

  val personDb = List(Reid, Lauren, Brooklyn, Levi, Matt, Bennett)

}
