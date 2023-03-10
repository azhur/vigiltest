package io.azhur.vigiltest

import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.funsuite.AnyFunSuite

class FiltersSpec extends AnyFunSuite with SharedSparkContext {
  test("should filter in odd key-value occurrences") {
    val input =
      sc.parallelize(
        Seq(
          1 -> 2,
          1 -> 3,
          2 -> 10,
          1 -> 2,
          1 -> 2,
          2 -> 10,
          3 -> 15,
          1 -> 3
        )
      )

    val expectedOutput = Set(
      1 -> 2,
      3 -> 15
    )

    val output = Filters.oddOccurrences(input).collect().toSet

    assert(expectedOutput == output)
  }
}
