package com.exercises.partyandclaim.spark_impl

import java.sql.Date

import org.apache.spark.sql.functions.{col, lit}
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}

object PartyAndClaimSpark {

  val witnessRole = "witness"
  val victimRole = "victim"

  val claimIdCol = "claimId"
  val claimCreatDateCol = "creatDate"
  val claimCols = List(claimIdCol, claimCreatDateCol)

  val partyIdCol = "partyId"
  val fulNameCol = "fulName"
  val partyCols = List(partyIdCol, fulNameCol)

  val rolePartyIdCol = "rolePartyId"
  val roleClaimIdCol = "roleClaimId"
  val roleCol = "role"
  val roleCols = List(rolePartyIdCol, roleClaimIdCol, roleCol)

  def findFrequentWitnesses(givenClaimId: Long, claimDf: DataFrame, partyDf: DataFrame, roleDf: DataFrame): Long = {

    val givenClaimCreatDate: Date = claimDf
      .filter(col(claimIdCol) === givenClaimId)
      .select(claimCreatDateCol)
      .as(Encoders.DATE)
      .head()

    val prevClaimsDf = claimDf
      .filter(col(claimCreatDateCol) < givenClaimCreatDate)

    val givenClaimsWitnessPartyDf = roleDf
      .filter(col(roleCol) === lit(witnessRole))
      .join(claimDf, col(roleClaimIdCol) === col(claimIdCol))
      .filter(col(claimCreatDateCol) === givenClaimCreatDate)
      .select(col(rolePartyIdCol).as("p_id"))

    givenClaimsWitnessPartyDf
      .join(roleDf, col("p_id") === col(rolePartyIdCol))
      .join(prevClaimsDf, col(roleClaimIdCol) === col(claimIdCol))
      .select(rolePartyIdCol)
      .distinct()
      .count()
  }
}
