package com.xcompany.xproject.spark.streaming

import scala.collection.mutable

import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Calendar
import java.text.SimpleDateFormat

//http://spark.apache.org/docs/2.2.0/streaming-programming-guide.html#accumulators-broadcast-variables-and-checkpoints
object BroadcastWrapper {
    @volatile private var instance: Broadcast[Map[String, String]] = null
    private val map = mutable.LinkedHashMap[String, String]()

    def getUpdateInfo(): Map[String, String] = {
        val jedis_driver = RedisClient.pool.getResource
        println("=====GET_DRIVER")
        val is_update = jedis_driver.lpop("is_update")
        println("is_update: " + is_update)
        println("=====READ_DRIVER")

        //        if (null == is_update) {
        //            rdd.sparkContext.broadcast(is_update)
        //        }

        jedis_driver.close()
        println("=====CLOSE_DRIVER")

        map += ("is_update" -> is_update)
        val broadcast_info = jedis_driver.get("broadcast_info")
        map += ("broadcast_info" -> broadcast_info)
        map.toMap
    }

    def getInstance(sc: SparkContext): Broadcast[Map[String, String]] = {
        if (instance == null) {
            synchronized {
                if (instance == null) {
                    val updateInfo = getUpdateInfo()
                    // https://www.jianshu.com/p/95896d06a94d
                    if (Some(null) != updateInfo.get("is_update")) {
                        instance = sc.broadcast(updateInfo)
                    }
                }
            }
        }
        instance
    }

    def broadcastInfo(sc: SparkContext, blocking: Boolean = false): Unit = {
        val updateInfo = getUpdateInfo()
        // https://www.jianshu.com/p/95896d06a94d
        if (Some(null) != updateInfo.get("is_update")) {
            if (instance != null) {
                instance.unpersist(blocking)
            }
            instance = sc.broadcast(updateInfo)
        }

        //        val calendar = Calendar.getInstance()
        //        val date = calendar.getTime()
        //        val format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        //        val dateFormat = format.format(date)
        //        println("=====broadcat success: " + dateFormat)
    }

    //    private def writeObject(out: ObjectOutputStream): Unit = {
    //        out.writeObject(instance)
    //    }
    //
    //    private def readObject(in: ObjectInputStream): Unit = {
    //        instance = in.readObject().asInstanceOf[Broadcast[Map[String, String]]]
    //    }

}

