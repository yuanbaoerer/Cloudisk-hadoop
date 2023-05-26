package cn.shiep.ex3.invertedIndex;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author yuanbao
 * @Date 2023/5/5
 * @Description
 */
public class Reduce extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String fileList = new String();
        for (Text value : values) {
            fileList += value.toString() + ";";
        }
        result.set(fileList);
        context.write(key, result);
    }
}
