package cn.shiep.weatherAnalysis.monthTemperature.lowest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description
 */
public class ReduceTemperatureMonthLowest extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {
    //map<key:year,value:avgTemperatureOfDay>
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        double lowestTemp = Double.MAX_VALUE;
        Iterator<DoubleWritable> iterator = values.iterator();
        while (iterator.hasNext()){
            lowestTemp = Math.min(lowestTemp,iterator.next().get());
        }
        context.write(key,new DoubleWritable(lowestTemp));
    }
}
