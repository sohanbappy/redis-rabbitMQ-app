package com.bappy.cache.controller;

import com.bappy.cache.entity.PayLoad;
import com.bappy.cache.helper.RabbitHelper;
import com.bappy.cache.service.RabbitMQService;
import com.bappy.cache.service.RabbitReceiverGuardService;
import com.bappy.cache.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    RedisService redisService;


    @RequestMapping("/")
    public String sayHello() {
        return "Hello!!";
    }

    @RequestMapping("/fromRedis/all")
    public List<PayLoad> getPayLoadByPattern() {
        List<PayLoad> payLoadList;
        payLoadList = redisService.getPayLoadsByPatter("*");
        return payLoadList;
    }

    @RequestMapping("/fromRedis/byKey")
    public PayLoad getPayLoadByKey(@RequestParam("key") String key) {
        PayLoad payLoad;
        payLoad = redisService.getFromRedisByKey(key);
        return payLoad;
    }

    @RequestMapping("/toRedis/save")
    public String saveToRedis(@RequestBody PayLoad payLoad) {
        redisService.saveToRedisEntity(payLoad);
        return "Saved to redis!!!";
    }

    @RequestMapping("/rabbit/start")
    public String startRabbitMQ() {
        RabbitReceiverGuardService service = new RabbitReceiverGuardService();
        service.start();
        System.out.println("RabbitMQ-Reader Started to read from QUEUE: " + RabbitHelper.QUEUE_NAME);
        return "RabbitMQ-Reader Started to read!!!!";
    }

    @RequestMapping("/rabbit/stop")
    public String stopRabbitMQ() {
        RabbitReceiverGuardService service = new RabbitReceiverGuardService();
        service.flag = false;
        System.out.println("RabbitMQ-Reader Stopped");
        return "RabbitMQ-Reader Stopped!!!!";
    }

    @RequestMapping("/rabbit/sendData")
    public PayLoad sentToQueue(@RequestBody PayLoad payLoad) throws Exception {
        RabbitMQService.sendToQueue(String.valueOf(payLoad), RabbitHelper.QUEUE_NAME);
        return payLoad;
    }

}
