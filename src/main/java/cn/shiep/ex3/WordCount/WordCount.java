package cn.shiep.ex3.WordCount;

import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * @Author yuanbao
 * @Date 2023/3/31
 * @Description 统计文件当中各种文本出现的次数，以空格划分
 */
public class WordCount {

    static HdfsUtils hdfsUtils = new HdfsUtils();

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        //给job起名字和配置信息
        //创建一个job对象，它表示一个MapReduce任务，可以通过job来指定任务的输入输出、
        //Map和Reduce函数等信息
        Job job = new Job(configuration, "word count");
        //设置job的主类
        job.setJarByClass(WordCount.class);

        //设置Map、Combiner和Reduce函数。
        //Map函数将输入的文本数据转换为键值对，其中键是单词
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        System.out.println("请指定输入路径：");
        //指定输入路径
        // String inputFile = "hdfs://192.168.18.128:9000/user/input/AcWing算法基础课.md";
        String inputFile = br.readLine();
        FileInputFormat.addInputPath(job, new Path(inputFile));

        //指定输出路径
        System.out.println("请指定输出路径：");
        // String outputFile = "hdfs://192.168.18.128:9000/user/output";
        String outputFile = br.readLine();
        if (hdfsUtils.testDir(outputFile)){
            hdfsUtils.deleteDir(outputFile);
            FileOutputFormat.setOutputPath(job, new Path(outputFile));
        }else {
            FileOutputFormat.setOutputPath(job, new Path(outputFile));
        }
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
