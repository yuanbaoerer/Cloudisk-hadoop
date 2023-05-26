package cn.shiep.weatherAnalysis.yearTemperature.highest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class ReduceTemperatureYearHighest extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        double highestTemp = Double.MIN_VALUE;
        Iterator<DoubleWritable> iterator = values.iterator();
        while (iterator.hasNext()){
            highestTemp = Math.max(highestTemp,iterator.next().get());
        }
        context.write(key,new DoubleWritable(highestTemp));
    }
}
