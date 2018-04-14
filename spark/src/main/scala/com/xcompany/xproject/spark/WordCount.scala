package com.xcompany.xproject.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object WordCount {
    def main(args: Array[String]): Unit = {
//        println(args(0))
        //        println("Hello World!")
        val conf = new SparkConf().setAppName("WordCount")
//                    .setMaster("spark://node-01:7077")
//                    .setMaster("local")
//                    .set("spark.executor.memory", "1g")
//                    .set("spark.cores.max", "1")
        val sc = new SparkContext(conf)

        val lines = sc.textFile("file:///home/xxproject/workspace/xxhadoop/spark_data/")
        val words = lines.flatMap(line => line.split(" "))
        val wordCounts = words.map(word => (word, 1)).reduceByKey((a, b) => a + b)
        wordCounts.collect().foreach(println)
        wordCounts.partitions.length
//        wordCounts.saveAsTextFile("file:///tmp/output")

        sc.stop()
    }
}
