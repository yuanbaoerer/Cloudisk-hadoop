package cn.shiep.weatherAnalysis.yearTemperature.avg;

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
public class ReduceAvgTemperatureYear extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {

    //map<key:year,value:avgTemperatureOfDay>
    @Override
    // protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
    //     double sum = 0;
    //     Iterator<DoubleWritable> iterator = values.iterator();
    //     while (iterator.hasNext()){
    //         sum += iterator.next().get();
    //     }
    //     LocalDate localDate = LocalDate.parse(key.toString()+"-01-01");
    //     int lengthOfYear = localDate.lengthOfYear();
    //     double avgTemperatureOfYear = sum / lengthOfYear;
    //     context.write(key,new DoubleWritable(avgTemperatureOfYear));
    // }

    protected void reduce(Text key, Iterable<DoubleWritable> values,Context context) throws IOException, InterruptedException {
        double sum = 0;
        for (DoubleWritable val : values) {
            sum += val.get();
        }
        LocalDate localdate = LocalDate.parse(key.toString()+"-01-01");
        int lengthOfYear = localdate.lengthOfYear();
        double avgTemperature = sum / lengthOfYear;
        context.write(key,new DoubleWritable(avgTemperature));
    }
}
