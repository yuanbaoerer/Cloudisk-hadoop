package cn.shiep.weatherAnalysis.quarterTemperature.avg;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class MapAvgTemperatureQuarter extends Mapper<Object, Text,Text, DoubleWritable> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        LocalDate localDate = LocalDate.parse(tokens[0]);
        double lowTemp = Double.parseDouble(tokens[3]);
        double highTemp = Double.parseDouble(tokens[4]);
        DoubleWritable avgTemperatureOfQuarter = new DoubleWritable((lowTemp + highTemp) / 2);
        //要把123划分到第1季度，这样123的key就是1，应该以year-n季度作为key
        int monthValue = localDate.getMonthValue();
        String yearQuarter = localDate.getYear() + "-";
        //map<key:year-n>
        if (monthValue == 1 || monthValue == 2 || monthValue == 3){
            context.write(new Text(yearQuarter+1),avgTemperatureOfQuarter);
        }else if (monthValue == 4 || monthValue == 5 || monthValue == 6){
            context.write(new Text(yearQuarter+2),avgTemperatureOfQuarter);
        }else if (monthValue == 7 || monthValue == 8 || monthValue == 9){
            context.write(new Text(yearQuarter+3),avgTemperatureOfQuarter);
        }else {
            context.write(new Text(yearQuarter+4),avgTemperatureOfQuarter);
        }
    }
}
