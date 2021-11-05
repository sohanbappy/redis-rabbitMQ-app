package com.bappy.cache.service;

import com.bappy.cache.entity.PayLoad;
import com.bappy.cache.helper.RedisHelper;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    private static Gson gson = new Gson();


    public static void saveToRedisString(String str) {
        Jedis redis = RedisHelper.getRedis();
        //convert and save
        try {
            PayLoad payLoad = gson.fromJson(str, PayLoad.class);
            if (payLoad.getType().equalsIgnoreCase("temporary")) {
                redis.set("temp_" + payLoad.getId(), gson.toJson(payLoad));
                redis.expire("temp_" + payLoad.getId(), RedisHelper.TIMEOUT);
            } else {
                redis.set("perm_" + payLoad.getId(), gson.toJson(payLoad));
            }
            redis.close();
        } catch (Exception e) {
            System.out.println("Exception in Redis-save");
        }
    }

    public void saveToRedisEntity(PayLoad payLoad) {
        Jedis redis = RedisHelper.getRedis();
        //save
        try {
            if (payLoad.getType().equalsIgnoreCase("temporary")) {
                redis.set("temp_" + payLoad.getId(), gson.toJson(payLoad));
                redis.expire("temp_" + payLoad.getId(), RedisHelper.TIMEOUT);
            } else {
                redis.set("perm_" + payLoad.getId(), gson.toJson(payLoad));
            }
            redis.close();
        } catch (Exception e) {
            System.out.println("Exception in Redis-save");
        }
    }

    public PayLoad getFromRedisByKey(String key) {
        PayLoad payLoad = null;
        Jedis redis = RedisHelper.getRedis();
        String str = redis.get(key);
        if (!str.isEmpty() || str != null) {
            payLoad = gson.fromJson(str, PayLoad.class);
        }
        return payLoad;
    }

    public List<PayLoad> getPayLoadsByPatter(String pattern) {
        List<PayLoad> payLoadList = new ArrayList<>();
        Jedis redis = RedisHelper.getRedis();
        try {
            Set<String> objects = redis.keys(pattern);
            if (!objects.isEmpty()) {
                //convert
                for (String s : objects) {
                    PayLoad payLoad = gson.fromJson(s, PayLoad.class);
                    payLoadList.add(payLoad);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in Retrieving list from Redis");
        }
        return payLoadList;
    }


}
