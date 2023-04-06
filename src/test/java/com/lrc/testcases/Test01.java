package com.lrc.testcases;

import com.lrc.entries.CaseInfo;
import com.lrc.utils.EasyPoiExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @param
 * @author lrc
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
        List<CaseInfo> listDatas = EasyPoiExcelUtil.readExcel(0);
        return listDatas.toArray();
    }
}
