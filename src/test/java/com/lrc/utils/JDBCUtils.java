package com.lrc.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @author lrc
 * @create 2022/1/9
 * @return
 * @description
 **/
public class JDBCUtils {
    /**
     * 和数据库建立连接
     * @return 数据库连接对象
     */
    public static Connection getConnection()  {
        //定义数据库连接
        //MySql：jdbc:mysql://localhost:3306/DBName
        String url="jdbc:mysql://localhost:3306/lottery?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String user="root";
        String password="123456";
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }


    /**
     * 修改数据库数据操作（插入、修改、删除）
     * @param sql 要执行的sql语句
     */
    public static void updateData(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        try {
            queryRunner.update(conn,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭连接
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 查询单个字段的数据
     * @param sql 要执行的sql语句
     * @return 返回查询结果
     */
    public static Object querySingleData(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        Object data =null ;
        try {
            data = queryRunner.query(conn,sql,new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 查询所有的数据
     * @param sql 要执行的sql语句
     * @return 返回查询结果
     */
    public static List<Map<String,Object>> queryAllDatas(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        List<Map<String,Object>> data = null;
        try {
            data = queryRunner.query(conn,sql,new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return data;
    }
}
