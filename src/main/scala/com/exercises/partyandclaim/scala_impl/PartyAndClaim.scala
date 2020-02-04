package com.exercises.partyandclaim.scala_impl

import com.exercises.partyandclaim.scala_impl.models.Role.Witness
import com.exercises.partyandclaim.scala_impl.models.{Claim, Party, Role}

/**
 * There is Party and Claim.
 * Party is a participant of some insurance claim.
 * Party has id and full name.
 * Claim has id and creation_date.
 * For a particular claim party will have some role.
 * Role is witness, victim etc
 *
 * PartyAndClaim implements business rules on given domain models
 */

object PartyAndClaim {
  /**
   * @curClaim - given claim
   * @return claim witnesses who were witnesses in a previous claim
   */
  def findFrequentWitnesses(curClaim: Claim, roles: List[Role]): Int = {

    // find previous claim => claims that happened before curClaim
    val prevClaims: List[Claim] = roles
      .filter(_.claim.creatDate.isBefore(curClaim.creatDate))
      .map(_.claim)

    // parties who were witnesses in one of previous claims
    val prevWitnesses: List[Party] = prevClaims
      .flatMap(prevClaim => roles
        .filter(r => checkRole(r, prevClaim))
        .map(_.party))

    // parties who were witnesses in cur claim
    val curClaimWitnesses: List[Party] = roles
      .filter(r => checkRole(r, curClaim))
      .map(_.party)

    prevWitnesses.toSet.intersect(curClaimWitnesses.toSet).size
  }

  private def checkRole: (Role, Claim) => Boolean = (role: Role, claim: Claim) =>  {
      role.claim == claim && role.role == Witness
  }
}
