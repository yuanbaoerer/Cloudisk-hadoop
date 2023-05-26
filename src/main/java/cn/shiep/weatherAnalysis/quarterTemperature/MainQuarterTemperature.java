package cn.shiep.weatherAnalysis.quarterTemperature;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import cn.shiep.weatherAnalysis.quarterTemperature.avg.MapAvgTemperatureQuarter;
import cn.shiep.weatherAnalysis.quarterTemperature.avg.ReduceAvgTemperatureQuarter;
import cn.shiep.weatherAnalysis.quarterTemperature.highest.MapTemperatureQuarterHighest;
import cn.shiep.weatherAnalysis.quarterTemperature.highest.ReduceTemperatureQuarterHighest;
import cn.shiep.weatherAnalysis.quarterTemperature.lowest.MapTemperatureQuarterLowest;
import cn.shiep.weatherAnalysis.quarterTemperature.lowest.ReduceTemperatureQuarterLowest;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class MainQuarterTemperature {
    public static void main(String[] args) throws Exception{
        HdfsUtils hdfsUtils = new HdfsUtils();
        //计算每季度最高气温
        Conf conf = new Conf("MainLowestTemperatureQuarter",MainQuarterTemperature.class, MapTemperatureQuarterLowest.class, ReduceTemperatureQuarterLowest.class,Text.class,DoubleWritable.class);
        hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/quarterTemperature/lowest/output/");

        //计算每季度平均气温
        // Conf conf = new Conf("MainAvgTemperatureQuarter",MainQuarterTemperature.class, MapAvgTemperatureQuarter.class, ReduceAvgTemperatureQuarter.class, Text.class, DoubleWritable.class);
        // hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt","hdfs://192.168.18.128:9000/user/weatherAnalysis/quarterTemperature/avg/output/");
    }
}
