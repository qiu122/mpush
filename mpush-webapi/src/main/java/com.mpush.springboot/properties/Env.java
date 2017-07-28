package com.mpush.springboot.properties;

/**
 * Created by qiu.xiaolong on 2017/6/16.
 */
public class Env {
    private String carInfoPathDev;
    private String carInfoPathTest;
    private String carInfoPathProd;

    public String getCarInfoPathDev() {
        return carInfoPathDev;
    }

    public void setCarInfoPathDev(String carInfoPathDev) {
        this.carInfoPathDev = carInfoPathDev;
    }

    public String getCarInfoPathTest() {
        return carInfoPathTest;
    }

    public void setCarInfoPathTest(String carInfoPathTest) {
        this.carInfoPathTest = carInfoPathTest;
    }

    public String getCarInfoPathProd() {
        return carInfoPathProd;
    }

    public void setCarInfoPathProd(String carInfoPathProd) {
        this.carInfoPathProd = carInfoPathProd;
    }
}
