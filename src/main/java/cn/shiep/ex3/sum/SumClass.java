package cn.shiep.ex3.sum;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author yuanbao
 * @Date 2023/4/22
 * @Description
 */
public class SumClass {

    static HdfsUtils hdfsUtils = new HdfsUtils();

    /**
     * NullWritable是Writable的一个特殊类，实现方法为空实现，不从数据流中读数据，也不写入数据，
     * 只充当占位符，如在MapReduce中，如果你不需要使用键或值，你就可以将键或值声明为NullWritable,
     * NullWritable是一个不可变的单实例类型。
     */
    public static class SumMapper extends Mapper<LongWritable, Text,LongWritable, NullWritable>{
        public int sum = 0;

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            sum += Integer.parseInt(value.toString());
        }

        @Override
        protected void cleanup(Mapper<LongWritable, Text, LongWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum),NullWritable.get());
        }
    }

    public static class SumReducer extends Reducer<LongWritable,NullWritable,LongWritable, NullWritable>{
        public long sum = 0;

        @Override
        protected void reduce(LongWritable key, Iterable<NullWritable> values, Reducer<LongWritable, NullWritable, LongWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            sum += key.get();
        }

        @Override
        protected void cleanup(Reducer<LongWritable, NullWritable, LongWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            context.write(new LongWritable(sum),NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Conf conf = new Conf("Sum",SumClass.class,SumMapper.class,SumReducer.class,LongWritable.class, NullWritable.class);
        hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/Sum/input/sum.txt","hdfs://192.168.18.128:9000/user/Sum/output");
    }

}
