package cn.shiep.weatherAnalysis.yearTemperature;

import cn.shiep.weatherAnalysis.monthTemperature.highest.ReduceTemperatureMonthHighest;
import cn.shiep.weatherAnalysis.monthTemperature.lowest.ReduceTemperatureMonthLowest;
import cn.shiep.weatherAnalysis.yearTemperature.avg.MapAvgTemperatureYear;
import cn.shiep.weatherAnalysis.yearTemperature.avg.ReduceAvgTemperatureYear;
import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import cn.shiep.weatherAnalysis.yearTemperature.highest.MapTemperatureYearHighest;
import cn.shiep.weatherAnalysis.yearTemperature.lowest.MapTemperatureYearLowest;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description
 */
public class MainYearTemperature {
    public static void main(String[] args) throws Exception {
        HdfsUtils hdfsUtils = new HdfsUtils();

        //计算年最低温度
        Conf conf = new Conf("MainYearTemperatureLowest",MainYearTemperature.class, MapAvgTemperatureYear.class, ReduceAvgTemperatureYear.class,Text.class,DoubleWritable.class);
        hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/yearTemperature/avg/output/");

        //计算年最高温度
        // Conf conf = new Conf("MainYearTemperatureHighest",MainYearTemperature.class, MapTemperatureYearHighest.class, ReduceTemperatureMonthHighest.class,Text.class,DoubleWritable.class);
        // hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/yearTemperature/highest/output/");

        // Conf conf = new Conf("MainYearTemperature",MainYearTemperature.class, MapAvgTemperatureYear.class, ReduceAvgTemperatureYear.class, Text.class, DoubleWritable.class);
        // hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/yearTemperature/avg/output/");
    }
}
