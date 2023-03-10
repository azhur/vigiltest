package io.azhur.vigiltest

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite

class CsvSpec extends AnyFunSuite with DataFrameSuiteBase {

  private implicit lazy val sparkSession: SparkSession = spark

  test("should read int key-value csv data with comma delimiter") {
    import spark.implicits._

    val input = spark.createDataset(
      scala.io.Source
        .fromString(
          """
        |k,v
        |1,2
        |
        |3,4
        |5,inv
        |""".stripMargin.trim
        )
        .getLines()
        .toSeq
    )

    val expectedOutput = Set(
      1 -> 2,
      3 -> 4
    )

    val res = Csv.readKeyValues(Csv.Source.DataSet(input)).collect().toSet

    assert(res == expectedOutput)
  }

  test("should read int key-value csv data with tab delimiter") {
    import spark.implicits._

    val input = spark.createDataset(
      scala.io.Source
        .fromString(
          s"""
          |k\tv
          |1\t2
          |
          |3\t4
          |h\tw
          |""".stripMargin.trim
        )
        .getLines()
        .toSeq
    )

    val expectedOutput = Set(
      1 -> 2,
      3 -> 4
    )

    val res = Csv.readKeyValues(Csv.Source.DataSet(input)).collect().toSet

    assert(res == expectedOutput)
  }
}
