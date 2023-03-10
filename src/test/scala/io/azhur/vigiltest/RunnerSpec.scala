package io.azhur.vigiltest

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite

class RunnerSpec extends AnyFunSuite with DataFrameSuiteBase {
  private lazy implicit val sparkSession: SparkSession = spark

  test("read->filter->write") {
    val inputPath  = getClass.getResource("/csv").getPath + "/*"
    val outputPath = System.getProperty("java.io.tmpdir") + "csv-test"

    Runner.run(inputPath, outputPath)

    val expectedOutput = Set(2 -> 1, 3 -> 2, 14 -> 5, 13 -> 2)

    val output = Csv.readKeyValues(Csv.Source.Path(outputPath + "/*.csv"), header = false).collect().toSet

    assert(output == expectedOutput)
  }

}
