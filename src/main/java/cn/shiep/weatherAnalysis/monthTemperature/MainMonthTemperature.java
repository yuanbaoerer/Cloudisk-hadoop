package cn.shiep.weatherAnalysis.monthTemperature;

import cn.shiep.weatherAnalysis.monthTemperature.highest.MapTemperatureMonthHighest;
import cn.shiep.weatherAnalysis.monthTemperature.highest.ReduceTemperatureMonthHighest;
import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

/**
 * @Author yuanbao
 * @Date 2023/5/10
 * @Description
 */
public class MainMonthTemperature{
    public static void main(String[] args) throws Exception {
        HdfsUtils hdfsUtils = new HdfsUtils();
        String inputPath = "hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt";

        //求每月最高气温
        Conf conf = new Conf("MainHighestMonthTemperature",MainMonthTemperature.class, MapTemperatureMonthHighest.class, ReduceTemperatureMonthHighest.class, ReduceTemperatureMonthHighest.class,Text.class,DoubleWritable.class);
        hdfsUtils.exeCount(conf.getJob(),inputPath,"hdfs://192.168.18.128:9000/user/weatherAnalysis/monthTemperature/highest/output/");

        //求每月最低气温
        // Conf conf = new Conf("MainLowestMonthTemperature",MainMonthTemperature.class, MapTemperatureMonthLowest.class, ReduceTemperatureMonthLowest.class,Text.class,DoubleWritable.class);
        // hdfsUtils.exeCount(conf.getJob(),inputPath,"hdfs://192.168.18.128:9000/user/weatherAnalysis/monthTemperature/lowest/output/");

        //求每月平均气温
        // Conf conf = new Conf("MainAvgMonthTemperature",MainMonthTemperature.class, MapTemperatureMonth.class, ReduceTemperatureMonth.class,Text.class,DoubleWritable.class);
        // hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/monthTemperature/avg/output/");

    }
}
