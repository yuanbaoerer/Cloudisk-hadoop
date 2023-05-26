package cn.shiep.ex3.childParent;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @Author yuanbao
 * @Date 2023/5/5
 * @Description
 */
public class Map extends Mapper<Object, Text,Text,Text> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        String childName = new String();
        String parentName = new String();
        String relationType = new String();//左右表标识
        StringTokenizer itr = new StringTokenizer(value.toString());
        String[] values = new String[2];
        int i = 0;
        while (itr.hasMoreTokens()){
            values[i] = itr.nextToken();
            i++;
        }
        if (values[0].compareTo("child") != 0){
            childName = values[0];
            parentName = values[1];
            //输出左表
            relationType = "1";
            //左表将parent设置为key
            context.write(new Text(values[1]),new Text(relationType + "+" + childName + "+" + parentName));
            //右表将child设置为key
            relationType = "2";
            /**
             * 应该是这里有问题，这个没给reduce传进去，跟我之前想改进程序的问题是一样的？？
             */
            context.write(new Text(values[0]), new Text(relationType + "+"+ childName + "+" + parentName));             }
    }
}
