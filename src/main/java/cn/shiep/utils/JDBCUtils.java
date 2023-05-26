package cn.shiep.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private static DruidDataSource dataSource;
    private static ThreadLocal<Connection> conns = new ThreadLocal<>();

    static {

        try {
            Properties properties = new Properties();
            // 读取 jdbc2.properties 属性配置文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/jdbc2.properties"));

            // 从流中加载数据
            properties.load(bufferedReader);
            // 创建数据库连接池
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取数据库连接，如果已经有了数据库连接则直接从ThreadLocal中获取，否则就创建一个数据库连接并存到ThreadLocal中
     * @return
     */
    public static Connection getConnection() {

        Connection conn = conns.get();

        if (conn == null) {
            try {
                conn = dataSource.getConnection();
                conns.set(conn);                    // 保存到 ThreadLocal 对象中，供后面的 jdbc 操作使用
                conn.setAutoCommit(false);          // 设置为手动管理事务
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    /**
     * 提交事务，并关闭释放连接
     */
    public static void commitAndClose() {
        Connection connection = conns.get();
        if (connection != null) {        // 如果不等于null，说明之前使用过连接，操作过数据库
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //一定要执行remove操作，否则就会出错（因为Tomcat服务器底层使用了线程池技术）
        conns.remove();
    }

    /**
     * 回滚事务，并关闭释放连接
     */
    public static void rollbackAndClose() {
        Connection connection = conns.get();
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        conns.remove();
    }
}
