package cn.shiep.weatherAnalysis.weatherCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/8
 * @Description 统计每一年的不同天气出现次数
 */
public class Map extends Mapper<Object,Text, Text,IntWritable> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        //2011-01-01,星期六,多云~雨夹雪,2.0,6.0
        String[] tokens = value.toString().split(",");
        String weatherType = tokens[2];
        //该条map的日期
        LocalDate localDate = LocalDate.parse(tokens[0]);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();
        //key:(year,weatherType),value:1
        context.write(new Text(year+","+weatherType),new IntWritable(1));
    }
}
