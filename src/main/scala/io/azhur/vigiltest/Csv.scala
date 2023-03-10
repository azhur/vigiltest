package io.azhur.vigiltest

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Row, SaveMode, SparkSession}

/** Utilities to work with csv files.
  */
object Csv {
  private val Comma = ","
  private val Tab   = "\t"

  /** Source of the input csv data.
    */
  sealed trait Source
  object Source {
    // Path in the filesystem
    final case class Path(value: String)             extends Source
    // InMemory dataset: useful in tests
    final case class DataSet(value: Dataset[String]) extends Source
  }

  /** Read and parse Int csv data delimited either by comma or tab into key values. Empty lines and invalid ints will be skipped.
    */
  def readKeyValues(source: Source, header: Boolean = true)(implicit spark: SparkSession): RDD[(Int, Int)] = {
    val reader = spark.read
      .option("inferSchema", value = false)
      .option("delimiter", value = Tab)
      .option("header", header)

    val df = source match {
      case Source.Path(value)    => reader.csv(value)
      case Source.DataSet(value) => reader.csv(value)
    }

    df.rdd
      .flatMap(parseIntKeyValue)
  }

  /** Write Int key value pairs into a csv file
    */
  def writeKeyValues(rdd: RDD[(Int, Int)], path: String, delimiter: String = Tab)(implicit spark: SparkSession): Unit =
    spark
      .createDataFrame(rdd)
      .write
      .mode(SaveMode.Overwrite)
      .option("delimiter", delimiter)
      .csv(path)

  private def parseIntKeyValue(row: Row): Option[(Int, Int)] = try {
    val keyAsStr       = row.getString(0).trim
    val commaDelimiter = keyAsStr.contains(Comma)

    if (commaDelimiter) {
      val parts = keyAsStr.split(Comma)
      Some((parts.head.trim.toInt, parts.last.trim.toInt))
    } else {
      Some((row.getString(0).trim.toInt, row.getString(1).trim.toInt))
    }
  } catch {
    case _: Throwable => None
  }

}
