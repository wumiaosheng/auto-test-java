package com.wums.testcases;

import com.wums.entries.CaseInfo;
import com.wums.utils.EasyPoiExcelUtil;
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
public class Test01 {

    @Test(dataProvider = "readCases")
    public void test01(CaseInfo caseInfo){
        System.out.println(caseInfo);
    }

    @DataProvider
    public Object[] readCases(){
        List<CaseInfo> listDatas = EasyPoiExcelUtil.readExcel("E:\\api_testcases.xls",0);
        return listDatas.toArray();
    }
}
