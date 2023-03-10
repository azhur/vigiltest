## Task
You can find the requirements [here](DU-GeneralDataEngineerTechTest-080223-1108.pdf).

**Note**: Bonus points are not implemented.

## How-to
 - run the application: `sbt "run -i s3a://aws-spark-test/csv-in -o s3a://aws-spark-test/csv-out -p test-user"`
 - run test: `sbt test`

**Note**: You will need to run sbt commands on Java8.
