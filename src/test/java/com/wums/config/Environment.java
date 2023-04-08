package com.wums.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @param
 * @author wums
 * @create 2022/1/9
 * @return
 * @description 主要用于全局管理环境变量，模拟Jmeter变量存储操作
 **/
public class Environment {
    //声明并定义一个map（类似于JMeter的环境变量）
    public static Map<String,Object> envMap = new HashMap<String, Object>();
}
