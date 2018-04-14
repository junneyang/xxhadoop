package com.xcompany.xproject.spark.streaming

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

//http://blog.csdn.net/qq_26525215/article/details/60466222
//https://segmentfault.com/a/1190000005085077
object RedisClient {
    val host = "node-04"
    val port = 63791
    val timeout = 50000
    val password = "123456"
    val database = 0
    val maxTotal = 10
    val maxIdle = 5
    val maxWaitMillis = timeout
    val testOnBorrow = true
    
    val config = new JedisPoolConfig
    config.setMaxTotal(maxTotal)
    config.setMaxIdle(maxIdle)
    config.setMaxWaitMillis(maxWaitMillis)
    config.setTestOnBorrow(testOnBorrow)
    
    // must lazy
    lazy val pool = new JedisPool(config, host, port, timeout, password, database)

    lazy val hook = new Thread {
        override def run = {
            println("Execute hook thread: " + this)
            pool.destroy()
        }
    }
    sys.addShutdownHook(hook.run)
}
