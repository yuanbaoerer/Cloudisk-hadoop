package cn.shiep.Stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class StreamTester1 {

    public static void streamReduce(){
        //合并，字符串连接
        String concat = Stream.of("A","B","C","D").reduce("",String::concat);
        double minValue = Stream.of(-1.5,1.0,-3.0,-2.0).reduce(Double.MAX_VALUE,Double::min);
        int sumValue = Stream.of(1,2,3,4).reduce(Integer::sum).get();
        int compareValue = Stream.of(1,2,3,4).reduce(0,Integer::compareTo);
        System.out.println(compareValue);
        List<String> list = Arrays.asList("123","123124");
    }

    public static void main(String[] args) {
        streamReduce();
    }
}
