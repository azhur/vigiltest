package io.azhur.vigiltest

import scopt.OParser

/** Command-line configuration parameters. Look into parser for details.
  */
case class Config(inputPath: String, outputPath: String, credentialsProfile: Option[String])

object Config {
  private def empty: Config = Config(inputPath = "", outputPath = "", credentialsProfile = None)

  def parse(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser  = {
      import builder._
      OParser.sequence(
        programName("Csv key/value odd occurrence filter"),
        opt[String]('i', "inputPath")
          .required()
          .action((x, c) => c.copy(inputPath = x))
          .text("s3 inputPath containing csv files. example: s3a://aws-spark-test/csv-in"),
        opt[String]('o', "outputPath")
          .required()
          .action((x, c) => c.copy(outputPath = x))
          .text("s3 outputPath the result will be written to. example: s3a://aws-spark-test/csv-in"),
        opt[String]('p', "credentialsProfile")
          .optional()
          .action((x, c) => c.copy(credentialsProfile = Some(x)))
          .text("Optional AWS credentials profile name, defaults to DefaultAWSCredentialsProviderChain if missing.")
      )
    }

    OParser.parse(parser, args, Config.empty)
  }
}
