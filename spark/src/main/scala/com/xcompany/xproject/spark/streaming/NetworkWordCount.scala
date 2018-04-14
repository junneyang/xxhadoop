package com.xcompany.xproject.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream.toPairDStreamFunctions

object NetworkWordCount {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("NetworkWordCount")
//                    .setMaster("spark://node-01:7077")
//                    .setMaster("local")
//                    .set("spark.executor.memory", "1g")
//                    .set("spark.cores.max", "1")
        val ssc = new StreamingContext(conf, Seconds(10))
        val lines = ssc.socketTextStream("node-01", 9999)
        val words = lines.flatMap(_.split(" "))
        val pairs = words.map(word => (word, 1))
        val wordCounts = pairs.reduceByKey(_ + _)
        wordCounts.print()
        
     
        wordCounts.foreachRDD { rdd =>
              rdd.foreachPartition { partitionOfRecords => {
//                val connection = ConnectionPool.getConnection()
                partitionOfRecords.foreach(record => {
//                    val sql = "insert into streaming_itemcount(item,count) values('" + record._1 + "'," + record._2 + ")"
//                    val stmt = connection.createStatement
//                    stmt.executeUpdate(sql)
                       println(record)
                })
//                ConnectionPool.returnConnection(connection)
              }}
        }
        
        ssc.start()             // Start the computation
        ssc.awaitTermination()  // Wait for the computation to terminate
    }
}

