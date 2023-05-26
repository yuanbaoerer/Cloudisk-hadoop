package cn.shiep.ex3.dataSort;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author yuanbao
 * @Date 2023/4/13
 * @Description 数据排序
 * 这个实例仅仅要求对输入数据进行排序，熟悉MapReduce过程的读者会很快想到在MapReduce过程中就有排序，
 * 是否可以利用这个默认的排序，而不需要自己再实现具体的排序呢？答案是肯定的。
 * 但是在使用之前首先需要了解它的默认排序规则。它是按照key值进行排序的，
 * 如果key为封装int的IntWritable类型，那么MapReduce按照数字大小对key排序，
 * 如果key为封装为String的Text类型，那么MapReduce按照字典顺序对字符串排序。
 *
 * 了解了这个细节，我们就知道应该使用封装int的IntWritable型数据结构了。
 * 也就是在map中将读入的数据转化成 IntWritable型，然后作为key值输出（value任意）。
 * reduce拿到<key，value-list>之后，将输入的 key作为value输出，
 * 并根据value-list中元素的个数决定输出的次数。输出的key（即代码中的linenum）是一个全局变量，
 * 它统计当前key的位次。需要注意的是这个程序中没有配置Combiner，
 * 也就是在MapReduce过程中不使用Combiner。这主要是因为使用map和reduce就已经能够完成任务了。
 */
public class DataSort {
    static HdfsUtils hdfsUtils = new HdfsUtils();

    /**
     * 默认按照key值排序
     */
    public static class Map extends Mapper<Object, Text,IntWritable,IntWritable>{
        private static IntWritable data = new IntWritable();


        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            data.set(Integer.parseInt(line));
            context.write(data,new IntWritable(1));
        }
    }

    public static class Reduce extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
        private static IntWritable linenum = new IntWritable(1);

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Reducer<IntWritable, IntWritable, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
            //根据value-list中的元素的个数决定输出的次数
            for (IntWritable val:values){
                context.write(linenum,key);
                //linenum用于统计当前输出的位次
                linenum = new IntWritable(linenum.get() + 1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请指定输入路径：");
        String inputPath = br.readLine();
        System.out.println("请指定输出路径：");
        String outputPath = br.readLine();

        //这个程序中没有配置Combiner，是因为MapReduce过程中不是有Combiner，只是用map和reduce就能够完成任务
        Conf conf = new Conf("Data Sort",DataSort.class,Map.class,Reduce.class,IntWritable.class,IntWritable.class);

        hdfsUtils.exeCount(conf.getJob(),inputPath,outputPath);
    }
}
