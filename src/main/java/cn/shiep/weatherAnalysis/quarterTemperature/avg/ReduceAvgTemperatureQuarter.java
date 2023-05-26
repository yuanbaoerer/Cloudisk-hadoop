package cn.shiep.weatherAnalysis.quarterTemperature.avg;

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
public class ReduceAvgTemperatureQuarter extends Reducer<Text, DoubleWritable,Text,DoubleWritable> {
    //map<key:year-n,value:avgTemperatureOfDay>
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        double sum = 0;
        Iterator<DoubleWritable> iterator = values.iterator();
        while (iterator.hasNext()){
            sum += iterator.next().get();
        }
        int year = Integer.parseInt(key.toString().substring(0,4));
        int quarter = Integer.parseInt(key.toString().substring(5));
        double avgTemperatureOfQuarter = 0;
        int daysNum = 0;
        if (quarter == 1){
            if (isLeapYear(year)){
                daysNum = 31 + 29 + 31;
                avgTemperatureOfQuarter = sum / daysNum;
            }else {
                daysNum = 31 + 28 + 31;
                avgTemperatureOfQuarter = sum / daysNum;
            }
        }else if (quarter == 2){
            daysNum = 30 + 31 + 30;
            avgTemperatureOfQuarter = sum / daysNum;
        }else if (quarter == 3){
            daysNum = 31 + 31 + 30;
            avgTemperatureOfQuarter = sum / daysNum;
        }else {
            daysNum = 31 + 30 + 31;
            avgTemperatureOfQuarter = sum / daysNum;
        }
        context.write(key,new DoubleWritable(avgTemperatureOfQuarter));
    }

    public static boolean isLeapYear(int year){
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
