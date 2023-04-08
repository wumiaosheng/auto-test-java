package com.lrc.entries;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @param
 * @author lrc
 * @create 2022/1/9
 * @return
 * @description
 **/
@Data
public class CaseInfo {
    @Excel(name = "序号(caseId)")
    private int caseId;

    @Excel(name = "接口模块(interface)")
    private String interfaceName;

    @Excel(name = "用例标题(title)")
    private String title;

    @Excel(name = "请求头(requestHeader)")
    private String requestHeader;

    @Excel(name = "请求方式(method)")
    private String method;

    @Excel(name = "接口地址(url)")
    private String url;

    @Excel(name = "参数输入(inputParams)")
    private String inputParams;

    @Excel(name = "期望返回结果(expected)")
    private String expected;

    @Excel(name = "数据库断言")
    private String dbAssert;

    @Excel(name="提取表达式(extractExper)")
    private String extractExper;




}
