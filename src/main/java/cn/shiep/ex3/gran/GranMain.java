/**
 * 
 */
package cn.shiep.ex3.gran;

import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.*;

public class GranMain {
	public static void main(String[] args) throws Exception {
		HdfsUtils hdfsUtils = new HdfsUtils();
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Tab Conect");
		job.setJarByClass(GranMain.class);
		job.setMapperClass(GranMap.class);
		job.setReducerClass(GranReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		String inputfile = "hdfs://192.168.18.128:9000/user/ex3/STjoin/input/child-parent.txt";
		FileInputFormat.addInputPath(job, new Path(inputfile));
		String outputfile = "hdfs://192.168.18.128:9000/user/ex3/STjoin/output/";
		if(hdfsUtils.testDir(outputfile)) {
			hdfsUtils.deleteDir(outputfile);
		}
		FileOutputFormat.setOutputPath(job, new Path(outputfile));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
