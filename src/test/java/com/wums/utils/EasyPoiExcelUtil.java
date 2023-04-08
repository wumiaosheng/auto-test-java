package com.wums.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.wums.entries.CaseInfo;

import java.io.File;
import java.util.List;

/**
 * @param
 * @author lrc
 * @create 2022/1/9
 * @return
 * @description Excel解析工具类
 **/
public class EasyPoiExcelUtil {
    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 获取Excel里的所有行
     */
    public static List<CaseInfo> readExcel(String path,int num){
        //读取测试用例
        File file=new File(path);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        params.setStartSheetIndex(num);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }


    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 获取Excel里的指定行
     */
    public static List<CaseInfo> readExcelPart(String path,int num,int startNum,int readRows){
        //读取测试用例
        File file=new File(path);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        //读取指定页的Sheet
        params.setStartSheetIndex(num);
        //指定从第几行开始读取
        params.setStartRows(startNum);
        //指定读取几行数据
        params.setReadRows(readRows);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }

    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 从指定行开始读取下面全部用例
     */
    public static List<CaseInfo> readExcelPart(String path,int num,int startNum){
        //读取测试用例
        File file=new File(path);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        //读取指定页的Sheet
        params.setStartSheetIndex(num);
        //指定从第几行开始读取
        params.setStartRows(startNum);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }
}
