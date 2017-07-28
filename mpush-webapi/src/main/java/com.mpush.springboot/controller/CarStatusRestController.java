package com.mpush.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpush.core.mqtt.MqttService;
import com.mpush.core.mqtt.impl.MqttServiceImpl;
import com.mpush.springboot.bean.PushMsg;
import com.mpush.springboot.util.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 实现客户端模拟tbox接受车辆数据
 */
@RestController
public class CarStatusRestController {
    // todo 参考  https://github.com/a983132370/springboot-morphia.git
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static MqttService mqttService = new MqttServiceImpl();

    private AtomicInteger nextMesssage = new AtomicInteger(0);

    @ResponseBody
    @RequestMapping(value = "/api/init/realTimeInfo", method = RequestMethod.POST)
    public JSON setInitCarRealTimeStatus(@RequestBody String msg) {
        PushMsg message = JSONObject.parseObject(msg,PushMsg.class);
        return JSONResult.success(message);
    }

    @RequestMapping(value = "/api/query/carInfo", method = RequestMethod.GET)
    public JSON setInitCarRealTimeStatus() {
        return JSONResult.success("helloworld");
    }
}
