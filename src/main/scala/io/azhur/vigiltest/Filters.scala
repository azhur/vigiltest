package io.azhur.vigiltest

import org.apache.spark.rdd.RDD

object Filters {

  /** Filters in only key pairs that occur odd number
    */
  def oddOccurrences(keyValues: RDD[(Int, Int)]): RDD[(Int, Int)] =
    keyValues
      .map(_ -> 1)
      .reduceByKey(_ + _)
      .filter { case (_, count) => count % 2 == 1 }
      .keys
}
