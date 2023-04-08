package com.wums.testcases;

import com.wums.common.BaseTest;
import com.wums.common.constants.Contants;
import com.wums.config.Environment;
import com.wums.entries.CaseInfo;
import com.wums.utils.EasyPoiExcelUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @param
 * @author wums
 * @create 2022/1/9
 * @return
 * @description
 **/
public class Test02 extends BaseTest {
    @BeforeClass
    public void setUp(){
        //向环境变量设置key
        Environment.envMap.put("key",Contants.KEY);
    }

    @Test(dataProvider = "readCases")
    public void test01(CaseInfo caseInfo){
        //将测试用例做整体替换，只要遇到${}数据，就替换为环境变量中的实际数据
        caseInfo=paramsReplace(caseInfo);
        //发起请求
        Response res = request(caseInfo);
        //断言请求
        assertResponse(res,caseInfo);
        assertDB(caseInfo);
        //将测试用例的提取表达式保存到环境变量中
        extractToEnvironment(res,caseInfo);
    }



    @DataProvider
    //向测试用例提供Excel数据
    public Object[] readCases(){
        List<CaseInfo> listDatas = EasyPoiExcelUtil.readExcel("E:\\api_testcases.xls",0);
        return listDatas.toArray();
    }



}
