package cn.shiep.weatherAnalysis.yearTemperature.avg;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description 统计每年平均温度
 */
public class MapAvgTemperatureYear extends Mapper<Object,Text,Text,DoubleWritable> {

    //一个map代表一天的数据
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        LocalDate localDate = LocalDate.parse(tokens[0]);
        Text year = new Text(localDate.getYear()+"");
        double lowTemp = Double.parseDouble(tokens[3]);
        double highTemp = Double.parseDouble(tokens[4]);
        DoubleWritable avgTemperatureOfDay = new DoubleWritable((lowTemp + highTemp) / 2);
        context.write(year,avgTemperatureOfDay);
    }
}
