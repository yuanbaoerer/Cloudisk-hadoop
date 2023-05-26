package cn.shiep.weatherAnalysis.weatherCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author yuanbao
 * @Date 2023/5/8
 * @Description 统计每一年的不同类型天气的出现次数
 * map<key = (year,weatherTypoe), value = 1>
 */
public class Reduce extends Reducer<Text, IntWritable,Text, IntWritable> {

    private static boolean[] flags = new boolean[3000];
    private IntWritable result = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = key.toString().split(",");
        Integer year = Integer.parseInt(tokens[0]);
        String weatherType = tokens[1];
        int sum = 0;
        //当某一年份第一次出现，则把当前年份写下
        if (flags[year] == false){
            flags[year] = true;
            context.write(new Text(""+year),null);
        }
        for (IntWritable val : values){
            sum += val.get();
        }
        result.set(sum);
        context.write(new Text(weatherType),result);
    }
}
