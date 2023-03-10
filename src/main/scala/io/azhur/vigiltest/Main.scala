package io.azhur.vigiltest

import com.amazonaws.auth.{AWSCredentialsProvider, DefaultAWSCredentialsProviderChain}
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Main extends App {
  Config.parse(args) match {
    case Some(config) =>
      val conf = new SparkConf()
        .setMaster("local")
        .setAppName("Odd occurrences filter")

      implicit val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

      initAwsCredentials(config)

      Runner
        .run(
          inputPath = config.inputPath,
          outputPath = config.outputPath
        )

    case None =>
      System.exit(-1)
  }

  /** Loads and sets AWS credentials either from profile set in config or defaults to DefaultAWSCredentialsProviderChain.
    */
  private def initAwsCredentials(config: Config)(implicit spark: SparkSession): Unit = {
    val awsCredentials =
      config.credentialsProfile
        .fold[AWSCredentialsProvider](ifEmpty = DefaultAWSCredentialsProviderChain.getInstance())(profile =>
          new ProfileCredentialsProvider(profile)
        )
        .getCredentials

    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", awsCredentials.getAWSAccessKeyId)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", awsCredentials.getAWSSecretKey)
  }
}
