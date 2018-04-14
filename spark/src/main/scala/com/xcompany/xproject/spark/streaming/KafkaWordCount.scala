package com.xcompany.xproject.spark.streaming

import java.text.SimpleDateFormat
import java.util.Calendar

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream.toPairDStreamFunctions
import org.apache.spark.streaming.kafka.KafkaUtils

import kafka.serializer.StringDecoder


object KafkaWordCount {
    def main(args: Array[String]): Unit = {
        
        
        
        val conf = new SparkConf().setAppName("KafkaWordCount")
            //                    .setMaster("spark://node-01:7077")
//            .setMaster("local")
        //                    .set("spark.executor.memory", "1g")
        //                    .set("spark.cores.max", "1")
        val ssc = new StreamingContext(conf, Seconds(10))

        //        val topics = "order,order-r"
        //        val topics = Set("user_events")
        val topics = "order-r3"
        val brokers = "node-02:9092,node-03:9092,node-04:9092"
        val group = "streaming-kafka-group"
        val topicsSet = topics.split(",").toSet
        val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
            "serializer.class" -> "kafka.serializer.StringEncoder",
            "auto.offset.reset" -> "smallest",
            "group.id" -> group)
        val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
        //        http://blog.csdn.net/u013983450/article/details/52514456
        //        https://www.cnblogs.com/windlaughing/p/3241776.html
//        val messages = kafkaStream.flatMap(line => {
//            val data = JSONObject.fromObject(line._2)
//            Some(data)
//        })
//        ssc.checkpoint("checkpoint-directory")
        
        // Hold a reference to the current offset ranges, so it can be used downstream
//        var offsetRanges = Array.empty[OffsetRange]
        
//        kafkaStream.transform(rdd =>
//            offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//            rdd
//        ).map(_._2)
//        .foreachRDD(rdd => {
        // original log print
        kafkaStream.print()
        val lines = kafkaStream.map(_._2)
//        lines.foreachRDD(rdd => {
//            val offsets = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//            offsets.foreach(offset => println("=====" + offset.partition + ": OffSet:" + offset.untilOffset))
//
//        })
        val words = lines.flatMap(_.split(" "))
        val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
        wordCounts.print()
//        val messages_map = messages.map(x => (x.getString("user"), x.getInt("click"))).reduceByKey(_ + _)
        wordCounts.foreachRDD(rdd => {//driver
            //check update & broadcast info
            val calendar = Calendar.getInstance()
            println("=====broadcat start: " + calendar.getTime)
            val broadcastWrapper = BroadcastWrapper.getInstance(rdd.sparkContext)
            BroadcastWrapper.broadcastInfo(rdd.sparkContext, true)
            
            val date = calendar.getTime()
            val format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            val dateFormat = format.format(date)
            println("=====broadcat success: " + dateFormat)
            
            rdd.foreachPartition(partitionOfRecords => {//worker
                val jedis = RedisClient.pool.getResource
                println("=====GET_WORKER")
                
                val broadcastInfo = broadcastWrapper.value
                println("=====broadcastInfo: " + broadcastInfo)
                
                partitionOfRecords.foreach(pair => {
//                    val uid = pair._1
//                    val clickCount = pair._2
//                    val jedis = RedisClient.pool.getResource
                    val dbIndex = 0
                    jedis.select(dbIndex)
//                    jedis.hincrBy(clickHashKey, uid, clickCount)
                    jedis.lpush("wordCounts", pair.toString())
//                    RedisClient.pool.returnResource(jedis)  
//                    jedis.close()
                    println("=====PUT_WORKER")
                })
                jedis.close()
                println("=====CLOSE_WORKER")
            })

        })

//        messages.foreachPartition { partitionOfRecords =>
//            {
//                //                val connection = ConnectionPool.getConnection()
//                partitionOfRecords.foreach(record => {
//                    //                    val sql = "insert into streaming_itemcount(item,count) values('" + record._1 + "'," + record._2 + ")"
//                    //                    val stmt = connection.createStatement
//                    //                    stmt.executeUpdate(sql)
//                    println(record)
//                })
//                //                ConnectionPool.returnConnection(connection)
//            }
//        }

        ssc.start()
        ssc.awaitTermination()
    }
}
