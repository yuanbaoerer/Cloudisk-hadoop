package cn.shiep.ex3.avg;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @Author yuanbao
 * @Date 2023/4/22
 * @Description
 */
public class ScoreAvgClass {

    static HdfsUtils hdfsUtils = new HdfsUtils();
    public static class Map extends Mapper<LongWritable,Text, Text, IntWritable>{

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            //map是逐行读的
            String line = value.toString();
            //将输入的数据首先按空格进行分割
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            while(stringTokenizer.hasMoreTokens()){
                String strName = stringTokenizer.nextToken();//学生姓名部分
                String strScore = stringTokenizer.nextToken();//学生成绩部分
                Text name = new Text(strName);
                int scoreInt = Integer.parseInt(strScore);
                context.write(name,new IntWritable(scoreInt));
            }
        }
    }

    public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable>{

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            //一个学生对应一个reduce
            int sum = 0;
            int count = 0;
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()){
                sum += iterator.next().get();
                count++;
            }
            int average = (int) sum / count;
            String averageString = Integer.toString(average);
            context.write(key,new IntWritable(average));
        }
    }

    public static void main(String[] args) throws Exception {
        Conf conf = new Conf("ScoreAvg",ScoreAvgClass.class,Map.class,Reduce.class,Reduce.class, Text.class, IntWritable.class);
        hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/ex3/ScoreAvg/input","hdfs://192.168.18.128:9000/user/ex3/ScoreAvg/output");
    }
}
