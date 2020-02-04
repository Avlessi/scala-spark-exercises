package com.exercises.partyandclaim.scala_impl

import java.time.LocalDate

import com.exercises.partyandclaim.scala_impl.models.Role.{Victim, Witness}
import com.exercises.partyandclaim.scala_impl.models.{Claim, Party, Role}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class PartyAndClaimTest extends AnyFlatSpec {

  val firstClaim: Claim = Claim(1L, LocalDate.of(2020, 10, 20))
  val secondClaim: Claim = Claim(2L, LocalDate.of(2020, 10, 19))
  val thirdClaim: Claim = Claim(3L, LocalDate.of(2020, 10, 18))

  val someParty: Party = Party(10L, "Sasha")
  val anotherParty: Party = Party(11L, "Jean")

  it should "work correctly given one party who witnesses in two claims" in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Witness),
      Role(someParty, thirdClaim, Witness)
    )

    val givenClaim: Claim = firstClaim
    val expectedWitnessesNumber = 1
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }

  it should "work correctly when there are no witnesses " in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Victim),
      Role(someParty, thirdClaim, Victim)
    )

    val givenClaim: Claim = firstClaim
    val expectedWitnessesNumber = 0
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }

  it should "work correctly given party who is witness in given claim and was not witness before" in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Witness),
      Role(someParty, secondClaim, Victim)
    )

    val givenClaim: Claim = firstClaim
    val expectedWitnessesNumber = 0
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }

  it should "work correctly given secondClaim as most recent claim" in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Witness),
      Role(someParty, secondClaim, Witness),
      Role(anotherParty, secondClaim, Witness),
      Role(anotherParty, firstClaim, Witness),
      Role(anotherParty, thirdClaim, Witness)
    )

    val givenClaim: Claim = secondClaim
    val expectedWitnessesNumber = 1 // here second claim is given, and it is anotherParty who was a witness
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }

  it should "work correctly with two witnesses" in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Witness),
      Role(anotherParty, firstClaim, Witness),
      Role(someParty, thirdClaim, Witness),
      Role(anotherParty, thirdClaim, Witness)
    )

    val givenClaim: Claim = firstClaim
    val expectedWitnessesNumber = 2
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }

  it should "work correctly" in {
    val roles: List[Role] = List(
      Role(someParty, firstClaim, Witness),
      Role(someParty, secondClaim, Victim),
      Role(anotherParty, secondClaim, Victim),
      Role(anotherParty, firstClaim, Victim),
      Role(someParty, thirdClaim, Witness)
    )

    val givenClaim: Claim = firstClaim
    val expectedWitnessesNumber = 1
    PartyAndClaim.findFrequentWitnesses(givenClaim, roles) shouldBe expectedWitnessesNumber
  }
}
