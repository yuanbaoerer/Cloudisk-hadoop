package cn.shiep.ex3.dedup;

import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.GenericOptionsParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @Author yuanbao
 * @Date 2023/4/7
 * @Description 数据去重
 */
public class Dedup {

    static HdfsUtils hdfsUtils = new HdfsUtils();

    /**
     * map将输入中的value复制到输出数据的key上，并直接输出
     */
    public static class Map extends Mapper<Object, Text,Text,Text>{
        private static Text line = new Text();//每行数据
        //实现map函数
        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
            //以每行的数据作为key，value值设空
            line = value;
            context.write(line,new Text(""));
        }
    }

    public static class Reduce extends Reducer<Text,Text,Text,Text>{

        /**
         * 一台reduce对应一个key，这样就实现了去重，每一台reduce把其对应的key输出就是实现了数据去重
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            context.write(key,new Text(""));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapred.job.tracker","hdfs://192.168.18.128:9000");
        String[] ioArgs = new String[]{"input","output"};
        String[] otherArgs = new GenericOptionsParser(conf,ioArgs).getRemainingArgs();
        if (otherArgs.length != 2){
            System.err.println("Usage:Data Deduplication <in> <out>");
            System.exit(2);
        }

        Job job = new Job(conf,"Data deduplication");
        job.setJarByClass(Dedup.class);

        //设置Map、Combine和Reduce处理类
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        //设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请指定输入路径：");
        String inputPath = br.readLine();
        System.out.println("请指定输出路径：");
        String outputPath = br.readLine();
        hdfsUtils.exeCount(job,inputPath,outputPath);
        //
        // //设置输入和输出目录
        // //指定输入路径
        // // String inputFile = "hdfs://192.168.18.128:9000/user/input";
        // String inputFile = br.readLine();
        // FileInputFormat.addInputPath(job, new Path(inputFile));
        //
        // //指定输出路径
        // System.out.println("请指定输出路径：");
        // // String outputFile = "hdfs://192.168.18.128:9000/user/output";
        // String outputFile = br.readLine();
        // if (hdfsUtils.testDir(outputFile)){
        //     hdfsUtils.deleteDir(outputFile);
        //     FileOutputFormat.setOutputPath(job, new Path(outputFile));
        // }else {
        //     FileOutputFormat.setOutputPath(job, new Path(outputFile));
        // }
        //
        // System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
