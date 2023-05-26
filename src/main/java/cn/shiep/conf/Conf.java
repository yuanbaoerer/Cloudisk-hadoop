package cn.shiep.conf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @Author yuanbao
 * @Date 2023/4/13
 * @Description
 */
public class Conf {
    Configuration conf = new Configuration();
    Job job;

    public Configuration getConf() {
        return conf;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * 不带CombineClass
     * @param jobName
     * @param jarByClass
     * @param mapperClass
     * @param reducerClass
     * @param outputKeyClass
     * @param outPutValueClass
     * @throws IOException
     */
    public Conf(String jobName,Class jarByClass,Class mapperClass,Class reducerClass,Class outputKeyClass,Class outPutValueClass) throws IOException {
        conf.set("mapred.job.tracker","hdfs://192.168.18.128:9000");
        job = new Job(conf,jobName);
        job.setJarByClass(jarByClass);

        //设置Map、Combine和Reduce处理类
        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);

        //设置输出类型
        job.setOutputKeyClass(outputKeyClass);
        job.setOutputValueClass(outPutValueClass);
    }

    public Conf(String jobName,Class jarByClass,Class mapperClass,Class combineClass,Class reducerClass,Class outputKeyClass,Class outPutValueClass) throws IOException {
        conf.set("mapred.job.tracker","hdfs://192.168.18.128:9000");
        job = new Job(conf,jobName);
        job.setJarByClass(jarByClass);

        //设置Map、Combine和Reduce处理类
        job.setMapperClass(mapperClass);
        job.setCombinerClass(combineClass);
        job.setReducerClass(reducerClass);

        //设置输出类型
        job.setOutputKeyClass(outputKeyClass);
        job.setOutputValueClass(outPutValueClass);
    }
}
