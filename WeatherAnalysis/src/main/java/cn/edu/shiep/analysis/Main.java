package cn.edu.shiep.analysis;

import cn.edu.shiep.conf.Conf;
import cn.edu.shiep.utils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author yuanbao
 * @Date 2023/5/8
 * @Description
 */
public class Main {
    public static void main(String[] args) throws Exception{
        HdfsUtils hdfsUtils = new HdfsUtils();
        Configuration conf = new Configuration();
        Job job = new Job(conf,"Weather Count");
        job.setJarByClass(Main.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        String inputfile = "hdfs://192.168.18.128:9000/user/weatherAnalysis/input/2011.1---2021.3天气数据.txt";
        FileInputFormat.addInputPath(job,new Path(inputfile));
        String outputfile = "hdfs://192.168.18.128:9000/user/weatherAnalysis/output/";
        if (hdfsUtils.testDir(outputfile)){
            hdfsUtils.deleteDir(outputfile);
        }
        FileOutputFormat.setOutputPath(job,new Path(outputfile));
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
