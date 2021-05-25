name := "KafkaSparkStreaming"

version := "0.1"

scalaVersion := "2.12.6"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.1.1"

// https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.1.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
libraryDependencies += "org.apache.spark" %% "spark-hive" % "3.1.1" % "provided"
// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
//libraryDependencies += "org.apache.spark" %% "spark-hive" % "3.0.1" % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.7"
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.2"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.1.1" % Test

// https://mvnrepository.com/artifact/org.apache.spark/spark-hive-thriftserver
libraryDependencies += "org.apache.spark" %% "spark-hive-thriftserver" % "2.4.7" % "provided"



//// https://mvnrepository.com/artifact/org.apache.spark/spark-core
//libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.4"
//
//// https://mvnrepository.com/artifact/com.github.biopet/spark-utils
////libraryDependencies += "com.github.biopet" %% "spark-utils" % "0.1"
//
//// https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
//libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"
//
//// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-10
//libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.4"
//
//// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.2"
//
//// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
//libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.4.4" % "provided"
//
//// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming
//libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.7" % "provided"
//
//// https://mvnrepository.com/artifact/com.databricks/spark-xml
////libraryDependencies += "com.databricks" %% "spark-xml" % "0.4.1"




















