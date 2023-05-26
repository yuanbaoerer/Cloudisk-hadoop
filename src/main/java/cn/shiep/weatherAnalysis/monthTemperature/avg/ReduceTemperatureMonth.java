package cn.shiep.weatherAnalysis.monthTemperature.avg;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description
 */
public class ReduceTemperatureMonth extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {
    //map<key:yearMonth,value:avgTemperatureOfDay>
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        double sum = 0;
        Iterator<DoubleWritable> iterator = values.iterator();
        while (iterator.hasNext()){
            sum += iterator.next().get();
        }
        LocalDate localDate = LocalDate.parse(key.toString()+"-01");
        int lengthOfMonth = localDate.lengthOfMonth();
        double avgTemperatureOfMonth = sum / lengthOfMonth;
        context.write(key,new DoubleWritable(avgTemperatureOfMonth));
    }
}
