package cn.shiep.weatherAnalysis.quarterTemperature.lowest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/13
 * @Description
 */
public class MapTemperatureQuarterLowest extends Mapper<Object, Text,Text, DoubleWritable> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        LocalDate localDate = LocalDate.parse(tokens[0]);
        String yearQuarter = localDate.getYear() + "-";
        DoubleWritable lowTemp = new DoubleWritable(Double.parseDouble(tokens[4]));
        int monthValue = localDate.getMonthValue();
        //map<key:year-n>
        if (monthValue == 1 || monthValue == 2 || monthValue == 3){
            context.write(new Text(yearQuarter+1),lowTemp);
        }else if (monthValue == 4 || monthValue == 5 || monthValue == 6){
            context.write(new Text(yearQuarter+2),lowTemp);
        }else if (monthValue == 7 || monthValue == 8 || monthValue == 9){
            context.write(new Text(yearQuarter+3),lowTemp);
        }else {
            context.write(new Text(yearQuarter+4),lowTemp);
        }
    }
}
