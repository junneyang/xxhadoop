package com.xcompany.xproject.spark.streaming

object WaitForReady {
    private val PREFIX = "streaming"
    private val IS_READY = PREFIX + ":is_ready"
    
    def waitForReady(): Unit = {
        val jedis_main = RedisClient.pool.getResource
        var is_ready = jedis_main.get(IS_READY)
            print(is_ready.toBoolean)
        while (null == is_ready) {
            println("is_ready: " + is_ready + ", continue waitFor...")
            Thread.sleep(5000)
            is_ready = jedis_main.get("is_ready")
        }
        jedis_main.close()
        println("is_ready: " + is_ready + ", start to submitJob...")        
    }
    
    def main(args: Array[String]): Unit = {
      waitForReady()
    }
}

