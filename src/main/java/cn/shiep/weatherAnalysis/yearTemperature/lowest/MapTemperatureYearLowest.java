package cn.shiep.weatherAnalysis.yearTemperature.lowest;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class MapTemperatureYearLowest extends Mapper<Object, Text,Text, DoubleWritable> {
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        LocalDate localDate = LocalDate.parse(tokens[0]);
        Text year = new Text(tokens[0].substring(0,4));
        double lowTemp = Double.parseDouble(tokens[4]);
        context.write(year,new DoubleWritable(lowTemp));
    }
}
