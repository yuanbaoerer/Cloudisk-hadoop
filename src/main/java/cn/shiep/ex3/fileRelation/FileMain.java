/**
 * 
 */
package cn.shiep.ex3.fileRelation;

import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.*;

public class FileMain {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		HdfsUtils hdfsUtils = new HdfsUtils();
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Tab Conect");
		job.setJarByClass(FileMain.class);//��������
		job.setMapperClass(FileMap.class); //ָ��mapper��
		job.setCombinerClass(FileCombiner.class);
		job.setReducerClass(FileReducer.class); //ָ��reducer��
		job.setOutputKeyClass(Text.class); //ָ�������key����������
		job.setOutputValueClass(Text.class);//ָ�����value��������
		String inputfile = "hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/input/";
		FileInputFormat.addInputPath(job, new Path(inputfile));
		String outputfile = "hdfs://192.168.18.128:9000/user/ex3/InvertedIndex/output/";
		if(hdfsUtils.testDir(outputfile)) {
			hdfsUtils.deleteDir(outputfile);
		}
		FileOutputFormat.setOutputPath(job, new Path(outputfile));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
