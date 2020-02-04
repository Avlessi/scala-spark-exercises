package com.exercises.partyandclaim.spark_impl

import java.sql.Date

import com.exercises.partyandclaim.spark_impl.models.{Claim, Party, Role}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import PartyAndClaimSpark.{witnessRole, victimRole}

class PartyAndClaimSparkTest extends AnyFlatSpec with BeforeAndAfterEach {
  var spark: SparkSession = _
  var claimDf: DataFrame = _
  var partyDf: DataFrame = _
  var roleDf: DataFrame = _

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

  override def beforeEach(): Unit = {
    spark = SparkSession.builder()
      .appName("PartyAndClaimSpark")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val sc = spark
    import sc.implicits._ // can't do directly on spark var since stable identifier required

    claimDf = Seq(
      Claim(1L, Date.valueOf("2020-10-20")),
      Claim(2L, Date.valueOf("2020-10-19")),
      Claim(3L, Date.valueOf("2020-10-18"))
    ).toDF(claimCols: _*)

    partyDf = Seq(
      Party(10L, "Sasha"),
      Party(11L, "Jean")
    ).toDF(partyCols: _*)
  }

  it should "work correctly given 3 roles with witnesses - one in cur claim and others in previous ones" in {
    val sc = spark
    import sc.implicits._
    roleDf = Seq(
      Role(10L, 1L, witnessRole),
      Role(10L, 2L, witnessRole),
      Role(10L, 3L, witnessRole)
    ).toDF(roleCols: _*)
    val givenClaimId = 1L
    val expectedRes = 1
    PartyAndClaimSpark.findFrequentWitnesses(givenClaimId, claimDf, partyDf, roleDf) shouldBe expectedRes
  }

  it should "work correcly given roles with one witness in cur claim and one victim in previous ones" in {
    val sc = spark
    import sc.implicits._
    roleDf = Seq(
      Role(10L, 1L, victimRole),
      Role(10L, 3L, witnessRole)
    ).toDF(roleCols: _*)
    val givenClaimId = 1L
    val expectedRes = 0
    PartyAndClaimSpark.findFrequentWitnesses(givenClaimId, claimDf, partyDf, roleDf) shouldBe expectedRes
  }

  it should "work correctly with two witnesses" in {
    val sc = spark
    import sc.implicits._
    roleDf = Seq(
      Role(10L, 1L, witnessRole),
      Role(11L, 1L, witnessRole),
      Role(10L, 3L, witnessRole),
      Role(11L, 3L, witnessRole)
    ).toDF(roleCols: _*)
    val givenClaimId = 1L
    val expectedRes = 2
    PartyAndClaimSpark.findFrequentWitnesses(givenClaimId, claimDf, partyDf, roleDf) shouldBe expectedRes
  }

  override def afterEach(): Unit = {
    spark.stop()
  }
}
