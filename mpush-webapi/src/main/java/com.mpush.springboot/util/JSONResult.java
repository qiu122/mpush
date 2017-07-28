package com.mpush.springboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by qiu.xiaolong on 2017/6/16.
 */
public class JSONResult extends JSON{

    private final static String SUCCESS_MSG = "success";
    private final static String FAILURE_MSG = "failure";
    private final static Integer SUCCESS_CODE = 1;
    private final static Integer FAILURE_CODE = 0;

    public static JSON success(Object data){
        JSONObject json = new JSONObject();
        json.put("data",data);
        json.put("msg",SUCCESS_MSG);
        json.put("code",SUCCESS_CODE);
        return json;
    }

    public static JSON failure(String msg){
        JSONObject json = new JSONObject();
        json.put("msg",msg);
        json.put("code",SUCCESS_CODE);
        return json;
    }
}
