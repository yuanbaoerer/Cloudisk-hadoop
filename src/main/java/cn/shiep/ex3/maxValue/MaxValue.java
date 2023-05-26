package cn.shiep.ex3.maxValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;



/**
 * @Author yuanbao
 * @Date 2023/4/14
 * @Description 通过hadoop求最大值
 */
public class MaxValue {

    static HdfsUtils hdfsUtils = new HdfsUtils();
    public static class MapClass extends Mapper<LongWritable, Text,Text, IntWritable>{
        private int maxNum = 0;
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] str = value.toString().split(" ");
            try {        // 对于非数字字符我们忽略掉
                for(int i=0;i<str.length;i++){
                    int temp = Integer.parseInt(str[i]);
                    if (temp > maxNum) {
                        maxNum = temp;
                    }
                }
            } catch (NumberFormatException e) {
            }
        }

        /**
         * 在Hadoop MapReduce中，Mapper和Reducer都具有一个特殊的方法：cleanup()，它们在Mapper或Reducer对象被销毁时自动调用，用于清理资源和状态。
         *
         * 在上面的代码中，cleanup()方法的作用是在Map任务完成后，将最大值maxNum写入上下文对象Context中。这样，Reducer任务就可以在收到来自所有Map任务的结果之后，找到全局最大值。
         *
         * 在这个例子中，cleanup()方法是非常简单的，只是将最大值写入上下文对象，但是在其他情况下，cleanup()方法可以用来关闭文件或网络连接、释放内存或其他资源等。
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void cleanup(Context context) throws IOException,
                InterruptedException {
            context.write(new Text("Max"), new IntWritable(maxNum));
        }
    }

    public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable> {
        private int maxNum = Integer.MIN_VALUE;
        private Text one = new Text();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            for (IntWritable val : values){
                if (val.get() > maxNum){
                    maxNum = val.get();
                }
            }
            one = key;
        }
        @Override
        protected void cleanup(Context context) throws IOException,
                InterruptedException {
            context.write(one, new IntWritable(maxNum));
        }
    }

    // public int run(String[] args) throws Exception{
    //     Configuration conf = getConf();
    //     conf.set("mapred.jar","hdfs://192.168.18.128:9000");
    //     Job job = new Job(conf,"MaxNum");
    //     job.setJarByClass(MaxValue.class);
    //
    //     FileInputFormat.setInputPaths(job,new Path("hdfs://192.168.18.128:9000/user/MaxValue/input/MaxValueFile1.txt"));
    //     FileOutputFormat.setOutputPath(job,new Path("hdfs://192.168.18.128:9000/user/MaxValue/output"));
    //
    //     job.setMapperClass(MapClass.class);
    //     job.setCombinerClass(Reduce.class);
    //     job.setReducerClass(Reduce.class);
    //
    //     job.setInputFormatClass(TextInputFormat.class);
    //     job.setOutputFormatClass(TextOutputFormat.class);
    //     job.setOutputKeyClass(Text.class);
    //     job.setOutputValueClass(IntWritable.class);
    //     System.exit(job.waitForCompletion(true) ? 0 : 1);
    //     return 0;
    // }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请指定输入路径：");
        String inputPath = br.readLine();
        System.out.println("请指定输出路径：");
        String outputPath = br.readLine();

        Conf conf = new Conf("MaxValue",MaxValue.class,MapClass.class
                ,Reduce.class,Reduce.class,Text.class,IntWritable.class);
        hdfsUtils.exeCount(conf.getJob(),inputPath,outputPath);
    }
}

