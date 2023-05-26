package cn.shiep.utils;

import jdk.nashorn.internal.scripts.JD;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author yuanbao
 * @Date 2023/5/18
 * @Description
 */
public class JDBCUtilsTest {

    @Test
    public void getConnection() {
        System.out.println(JDBCUtils.getConnection());
    }
}