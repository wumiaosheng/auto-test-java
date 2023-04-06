package com.lrc.common;

import com.alibaba.fastjson.JSONObject;
import com.lrc.config.Contants;
import com.lrc.config.Environment;
import com.lrc.entries.CaseInfo;
import com.lrc.utils.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.LogConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @param
 * @author lrc
 * @create 2022/1/9
 * @return
 * @description
 **/
public class BaseTest {

    @BeforeSuite
    public void beforeMethod(){
        //把json小数的返回类型配置成BigDecimal类型，通过此配置，可以使得我们在断言小数类型的时候保持数据类型一致，避免了因数据类型不一致而导致断言不通过的情况
        RestAssured.config = RestAssured.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        //REST-assured基础 baseurl设置
        RestAssured.baseURI= Contants.BASE_URL;
    }

    /**
     * 封装所有请求类型
     * @param caseInfo 测试用例对象
     * @return response响应对象
     */
    public static Response request(CaseInfo caseInfo){
        //在用例基类每个请求添加日志
        String logFilepath="";
        //如果开关控制为false，即不在控制台输出日志，才创建日志文件
        if(!Contants.SHOW_CONSOLE_LOG) {
            //此处按照接口名称进行日志文件分类处理
            File dirFile = new File("logs\\" + caseInfo.getInterfaceName());
            if (!dirFile.exists()) {
                //如果文件及文件夹不存在，则创建文件及文件夹
                dirFile.mkdirs();
            }
            PrintStream fileOutPutStream = null;
            //日志文件路径
            logFilepath = "logs\\" + caseInfo.getInterfaceName() + "\\" + caseInfo.getInterfaceName() + "_" + caseInfo.getCaseId() + ".log";
            try {
                fileOutPutStream = new PrintStream(new File(logFilepath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //每个接口请求的日志单独的保存到本地的每一个文件中
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        String requestHeaders=caseInfo.getRequestHeader();
        Map requestHeadersMap= JSONObject.parseObject(requestHeaders);
        String url=caseInfo.getUrl();
        String params=caseInfo.getInputParams();
        String method=caseInfo.getMethod();
        Response response=null;
        if ("get".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all().headers(requestHeadersMap).when().get(url).then().log().all().extract().response();
        }
        else if ("post".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all().headers(requestHeadersMap).body(params).when().post(url).then().log().all().extract().response();
        }
        else if ("put".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all().headers(requestHeadersMap).body(params).when().post(url).then().log().all().extract().response();
        }
        //可以在此处添加想要的信息到日志文件中
        //请求结束之后将接口日志添加到allure报表中
        if(!Contants.SHOW_CONSOLE_LOG) {
            try {
                Allure.addAttachment("接口请求响应日志", new FileInputStream(logFilepath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 响应断言
     * @param res 实际响应结果
     * @param caseInfo 请求数据（实体类）
     */
    public void assertResponse(Response res,CaseInfo caseInfo){
        String expected = caseInfo.getExpected();
        if(expected != null) {
            //转成Map
            Map<String, Object> expectedMap = JSONObject.parseObject(expected);
            Set<String> allKeySet = expectedMap.keySet();
            for (String key : allKeySet) {
                //获取实际响应结果
                Object actualResult = res.jsonPath().get(key);
                //获取期望结果
                Object expectedResult = expectedMap.get(key);
                Assert.assertEquals(actualResult, expectedResult);
            }
        }
    }

    /**
     * 数据库断言统一封装
     * @param caseInfo 用例数据
     */
    public void assertDB(CaseInfo caseInfo){
        String dbAssertInfo = caseInfo.getDbAssert();
        if(dbAssertInfo != null) {
            Map<String, Object> mapDbAssert = JSONObject.parseObject(dbAssertInfo);
            Set<String> allKeys = mapDbAssert.keySet();
            for (String key : allKeys) {
                //key为对应要执行的sql语句
                Object dbActual = JDBCUtils.querySingleData(key);
                //根据数据库中读取实际返回类型做判断
                //1、Long类型
                if(dbActual instanceof Long){
                    Integer dbExpected = (Integer) mapDbAssert.get(key);
                    Long expected = dbExpected.longValue();
                    Assert.assertEquals(dbActual, expected);
                }else {
                    Object expected = mapDbAssert.get(key);
                    Assert.assertEquals(dbActual, expected);
                }
            }
        }
    }


    /**
     * 通过【提取表达式】将对应响应值保存到环境变量中
     * @param res 响应信息
     * @param caseInfo 实体类对象
     */
    public void extractToEnvironment(Response res, CaseInfo caseInfo){
        String extractStr = caseInfo.getExtractExper();
        if(extractStr != null) {
            //把提取表达式转成Map
            Map<String, Object> map = JSONObject.parseObject(extractStr);
            Set<String> allKeySets = map.keySet();
            for (String key : allKeySets) {
                //key为变量名，value是为提取的gpath表达式
                Object value = map.get(key);
                Object actualValue = res.jsonPath().get((String) value);
                //将对应的键和值保存到环境变量中
                Environment.envMap.put(key, actualValue);
            }
        }
    }


    /**
     * 正则替换功能，比如：
     * 原始字符串 {
     *   key=${key}
     * }
     * 替换为
     * {
     *   key=xxxx(自己账号生成的key)
     * }
     * xxxx 为环境变量中key变量名对应的变量值
     * @param orgStr 源字符串
     * @return
     */
    public String regexReplace(String orgStr){
        if(orgStr != null) {
            //匹配器
            Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
            //匹配对象
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            //循环遍历匹配对象
            while (matcher.find()) {
                //获取整个匹配正则的字符串 ${key}
                String allFindStr = matcher.group(0);
                //找到${XXX}内部的匹配的字符串 key
                String innerStr = matcher.group(1);
                //找到key：xxxx
                //具体的要替换的值（从环境变量中去找到的）
                Object replaceValue = Environment.envMap.get(innerStr);
                //要替换${key} --> xxxx
                result = result.replace(allFindStr, replaceValue + "");
            }
            return result;
        }else{
            return orgStr;
        }
    }

    /**
     * 整条用例数据的参数化替换，只要在对应的用例数据里面有${}包裹起来的数据,那么就会从环境变量中找，如果找到的话就去替换，否则不会
     * @param caseInfo
     */
    public CaseInfo paramsReplace(CaseInfo caseInfo){
        //1、请求头
        String requestHeader = caseInfo.getRequestHeader();
        caseInfo.setRequestHeader(regexReplace(requestHeader));
        //2、接口地址
        String url = caseInfo.getUrl();
        caseInfo.setUrl(regexReplace(url));
        //3、参数输入
        String inputParams = caseInfo.getInputParams();
        caseInfo.setInputParams(regexReplace(inputParams));
        //4、期望结果
        String expected = caseInfo.getExpected();
        caseInfo.setExpected(regexReplace(expected));
        return caseInfo;
    }


}
