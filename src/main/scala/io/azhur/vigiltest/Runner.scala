package io.azhur.vigiltest

import org.apache.spark.sql.SparkSession

object Runner {

  /** Runs the spark job:
    *   - Reads the input Csv files into an RDD;
    *   - Filters in only odd key-value occurrences;
    *   - Writes the filtered RDD as a Csv to the output path.
    */
  def run(inputPath: String, outputPath: String)(implicit spark: SparkSession): Unit = {

    val inputData = Csv.readKeyValues(Csv.Source.Path(inputPath))

    val oddOccurrences = Filters.oddOccurrences(inputData)

    Csv.writeKeyValues(oddOccurrences, outputPath)
  }

}
