package cn.shiep.ex3.invertedIndex;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @Author yuanbao
 * @Date 2023/5/5
 * @Description
 */
public class Map extends Mapper<Object, Text, Text, Text> {
    private Text keyInfo = new Text();
    private Text valueInfo = new Text();
    private org.apache.hadoop.mapreduce.lib.input.FileSplit split;
    // ʵ��map����
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        split = (FileSplit) context.getInputSplit();
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {

            int splitIndex = split.getPath().toString().indexOf("file");
            keyInfo.set(itr.nextToken() + ":" + split.getPath().toString().substring(splitIndex));
            valueInfo.set("1");
            context.write(keyInfo, valueInfo);
        }
    }
}