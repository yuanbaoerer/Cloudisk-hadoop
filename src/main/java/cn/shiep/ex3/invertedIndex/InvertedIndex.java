package cn.shiep.ex3.invertedIndex;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @Author yuanbao
 * @Date 2023/4/24
 * @Description
 * 最开始是把Map、Combiner、Reduce都是以内部类的形式写进主类的，但是执行之后没有输出结果。
 * 修改后：把上述的三个类单独写出来了，程序正常运行
 */
public class InvertedIndex {



    // public static class Map extends Mapper<Object, Text,Text,Text>{
    //     private Text keyInfo = new Text();  //存储单词和URL组合
    //     private Text valueInfo = new Text();    //存储词频
    //     /**
    //      * FileSplitFileSplit是Hadoop MapReduce框架中的一个类，
    //      * 用于将输入文件划分为多个数据块以便并行处理。FileSplit表示输入文件的一个分片，
    //      * 其中包含了该分片的起始位置、长度、所在节点等信息。
    //      */
    //     private FileSplit split;    //存储Split对象
    //
    //     @Override
    //     protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
    //         //获得<key,value>对所属的FileSplit对象
    //         split = (FileSplit) context.getInputSplit();
    //         StringTokenizer itr = new StringTokenizer(value.toString());
    //         while (itr.hasMoreTokens()){
    //             // key值由单词和URL组成，如"MapReduce：file1.txt"
    //             // 获取文件的完整路径
    //             // keyInfo.set(itr.nextToken()+":"+split.getPath().toString());
    //             //这里为了好看，只获取文件的名称
    //             int splitIndex = split.getPath().toString().indexOf("file");
    //             keyInfo.set(itr.nextToken() + ":" + split.getPath().toString().substring(splitIndex));
    //             //词频初始化为1
    //             valueInfo.set("1");
    //             context.write(keyInfo,valueInfo);
    //         }
    //     }
    // }

    // public static class Combine extends Reducer<Text,Text,Text,Text>{
    //     private Text info = new Text();
    //
    //     @Override
    //     protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
    //         //词频统计
    //         int sum = 0;
    //         for (Text value : values){
    //             sum += Integer.parseInt(value.toString());
    //         }
    //         int splitIndex = key.toString().indexOf(":");
    //         //重新设置value值由URL和词频组成
    //         info.set(key.toString().substring(splitIndex + 1) + ":" + sum);
    //         //重新设置key值为单词
    //         key.set(key.toString().substring(0,splitIndex));
    //         context.write(key,info);
    //     }
    // }



    // public static class Reduce extends Reducer<Text,Text,Text,Text>{
    //     private Text result = new Text();
    //
    //     @Override
    //     protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
    //         //生成文档列表
    //         String fileList = new String();
    //         for (Text value : values){
    //             fileList += value.toString() + ";";
    //         }
    //         result.set(fileList);
    //         context.write(key,result);
    //     }
    // }

    public static void main(String[] args) throws Exception {

        HdfsUtils hdfsUtils = new HdfsUtils();
        Configuration conf = new Configuration();
        Job job = new Job(conf,"Tab Connect");
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(Map.class);
        job.setCombinerClass(Combine.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        String inputfile = "hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/input/";
        FileInputFormat.addInputPath(job,new Path(inputfile));
        String outputfile = "hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output/";
        if (hdfsUtils.testDir(outputfile)){
            hdfsUtils.deleteDir(outputfile);
        }
        FileOutputFormat.setOutputPath(job,new Path(outputfile));
        System.exit(job.waitForCompletion(true) ? 0 : 1);

//         Configuration conf = new Configuration();
// // 这句话很关键
//         conf.set("mapred.job.tracker", "hdfs://192.168.18.128:9000");
//         String[] ioArgs = new String[] { "index_in", "index_out" };
//         String[] otherArgs = new GenericOptionsParser(conf, ioArgs)
//                 .getRemainingArgs();
//         if (otherArgs.length != 2) {
//             System.err.println("Usage: Inverted Index <in> <out>");
//             System.exit(2);
//         }
//         Job job = new Job(conf, "Inverted Index");
//         job.setJarByClass(InvertedIndex.class);
// // 设置Map、Combine和Reduce处理类
//         job.setMapperClass(Map.class);
//         job.setCombinerClass(Combine.class);
//         job.setReducerClass(Reduce.class);
// // 设置Map输出类型
//         job.setMapOutputKeyClass(Text.class);
//         job.setMapOutputValueClass(Text.class);
// // 设置Reduce输出类型
//         job.setOutputKeyClass(Text.class);
//         job.setOutputValueClass(Text.class);
// // 设置输入和输出目录
//         FileInputFormat.addInputPath(job, new Path("hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/input/"));
//         HdfsUtils hdfsUtils = new HdfsUtils();
//         if (hdfsUtils.testDir("hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output")){
//             hdfsUtils.deleteDir("hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output");
//             FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output"));
//         }else {
//             FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output"));
//         }
//         System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
