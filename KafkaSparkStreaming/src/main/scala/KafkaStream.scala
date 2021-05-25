
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.util.LongAccumulator
//import org.apache.hadoop.hbase.client.{ColumnFamilyDescriptorBuilder, ConnectionFactory, TableDescriptorBuilder}
//import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog


object KafkaStream {

//  case class NBARecord(playerName: String, teamName: String, gameDate:String, season:Int,
//                       playerId: Int, teamId: Int, espnGameId: Int, gamePeriod:Int, minRemaining: Int, secRemaining:Int,
//                       shotFlag: Int, actionType: String, shotType: String, shotDistance: String, opponent: String, courtX: Int, courtY: Int)


  def main(args: Array[String]): Unit = {

    //Variable to be used for Kafka setup
    val brokers = "127.0.0.1:9092"
    val groupid = "spark-streaming-nba"
    val kafkaTopic = "test"

    val batchesToRun = 5000
    val batchInterval = Seconds(3)
    //val warehouseLocation = "/user/hive/warehouse" //hdfs://localhost:9000/user/hive/warehouse"

    val SparkConf = new SparkConf().setMaster("local").setAppName("KafkaSparkStreaming").set("spark.sql.catalogImplementation","hive")
      .set("parquet.enable.summary-metadata", "false")
      //.set("spark.sql.warehouse.dir", warehouseLocation)

    val ssc = new StreamingContext(SparkConf, batchInterval)
    val sc = ssc.sparkContext


    val spark = SparkSession.builder().config(sc.getConf)
      .config ("spark.sql.warehouse.dir", "hdfs://localhost:8020:/user/hive/warehouse")
      .config("hive.metastore.uris", "thrift://localhost:9083")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .enableHiveSupport().getOrCreate()



    import spark.implicits._


    //Creating Schema for the NBA Records
    val schema = StructType(
        StructField("playerName", StringType, false) ::
        StructField("teamName", StringType, false) ::
        StructField("gameDate", StringType, false) ::
        StructField("season", IntegerType, false) ::
        StructField("playerId", IntegerType, false) ::
        StructField("teamId", IntegerType, false) ::
        StructField("espnGameId", IntegerType, false) ::
        StructField("gamePeriod", IntegerType, false) ::
        StructField("minRemaining", IntegerType, false) ::
        StructField("secRemaining", IntegerType, false) ::
        StructField("shotFlag", IntegerType, false) ::
        StructField("actionType", StringType, false) ::
        StructField("shotType", StringType, false) ::
        StructField("shotDistance", IntegerType, false) ::
        StructField("opponent", StringType, false) ::
        StructField("courtX", IntegerType, false) ::
        StructField("courtY", IntegerType, false) ::Nil

    )


    object BatchCounter {
      @volatile private var instance: LongAccumulator = null

      def getInstance(sc: SparkContext): LongAccumulator = {
        if (instance == null) {
          synchronized {
            if (instance == null) {
              instance = sc.longAccumulator("BatchesCounter")
            }
          }
        }
        instance
      }
    }

    /************************************** Kafka Setup ***************************************/
    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupid,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )

    val topicSet = kafkaTopic.split(",").toSet
    val recordStream = KafkaUtils.createDirectStream[String, String](
            ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String,String](topicSet, kafkaParams)
    )
    /************************************** Kafka Setup ***************************************/

    val nbajson = recordStream.map(_.value())
    nbajson.foreachRDD {rdd =>

      val rawDF = rdd.toDF("nbarecords")
      val df = rawDF.select(from_json($"nbarecords", schema ) as "data").select("data.*")

      //cache for to make it faster
      df.cache()
      df.show()

      df.createOrReplaceTempView("nbarecords")
      spark.sql("CREATE TABLE IF NOT EXISTS nbarecords AS SELECT * from nbarecords" )

      df.write.mode("append").format("hive").saveAsTable("nbarecords")
      spark.sql("select * from dbnba").show()

      val curryDurant = df.filter("playerName = 'Stephen Curry' OR playerName = 'Kevin Durant' AND shotType = '3PT Field Goal'")
      curryDurant.drop("playerId", "teamId", "espnGameId", "minRemaining", "secRemaining", "shotDistance")
      curryDurant.createOrReplaceTempView("curryDurant")
      spark.sql("CREATE TABLE IF NOT EXISTS curryDurant AS SELECT * from curryDurant" )
      if (!curryDurant.isEmpty){
        curryDurant.coalesce(1).write.mode("append").format("csv")
          .saveAsTable("curryDurant")
      }

      curryDurant.cache()

      //Spark SQL query
      val top3pointersDF = spark.sql("SELECT playerName, sum(shotFlag) as total FROM nbarecords WHERE shotType = '3PT Field Goal'" +
        " GROUP By playerName ORDER BY total DESC")
      if(!top3pointersDF.isEmpty){
        println("----- top 3 pointers ------")
        top3pointersDF.show();
      }

      top3pointersDF.cache()


      val batchCounter = BatchCounter.getInstance(sc)
      println(s"--- Batch ${batchCounter.count + 1} ---")
      println("Processed messages in this batch: " + df.count())

      if (batchCounter.count >= batchesToRun - 1) {
        ssc.stop()
      } else {
        batchCounter.add(1)
      }
    }

    ssc.start()
    ssc.awaitTermination()

  }
}
