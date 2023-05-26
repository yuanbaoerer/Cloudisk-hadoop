package cn.shiep.utils;

import java.io.InputStream;

/**
 * @Author yuanbao
 * @Date 2023/5/19
 * @Description
 */
public class Test {
    public static void main(String[] args) throws Exception {
        InputStream br = Test.class.getResourceAsStream("jdbc2.properties");
        System.out.println(br.read());
    }
}
