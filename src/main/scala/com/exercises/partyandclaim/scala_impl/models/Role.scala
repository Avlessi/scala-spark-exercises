package com.exercises.partyandclaim.scala_impl.models

import com.exercises.partyandclaim.scala_impl.models.Role.RoleVal

case class Role(party: Party, claim: Claim, role: RoleVal)

object Role {
  sealed trait RoleVal
  case object Witness extends RoleVal
  case object Victim extends RoleVal
}