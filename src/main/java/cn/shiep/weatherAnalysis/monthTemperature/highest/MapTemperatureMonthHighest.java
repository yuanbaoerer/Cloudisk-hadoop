package cn.shiep.weatherAnalysis.monthTemperature.highest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description
 */
public class MapTemperatureMonthHighest extends Mapper<Object, Text,Text, DoubleWritable> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        LocalDate localDate = LocalDate.parse(tokens[0]);
        Text yearMonth = new Text(tokens[0].substring(0,7));
        double highTemp = Double.parseDouble(tokens[4]);
        context.write(yearMonth,new DoubleWritable(highTemp));
    }
}
