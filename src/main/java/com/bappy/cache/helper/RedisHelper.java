package com.bappy.cache.helper;

import org.apache.catalina.Host;
import redis.clients.jedis.Jedis;

public class RedisHelper {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;

    public static final int TIMEOUT = 300;//in seconds

    public static Jedis getRedis() {
        return new Jedis(HOST, PORT);
    }
}
