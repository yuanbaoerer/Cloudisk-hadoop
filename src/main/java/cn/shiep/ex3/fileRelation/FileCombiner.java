/**
 * 
 */
package cn.shiep.ex3.fileRelation;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer.Context;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FileCombiner extends Reducer<Text, Text, Text, Text> { 
	private static Text info = new Text();
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		int sum = 0; 
		for (Text value : values) { 
			sum += Integer.parseInt(value.toString()); 
		} 
		int splitIndex = key.toString().indexOf(":"); 
		info.set(key.toString().substring(splitIndex + 1) + ":" + sum);
		key.set(key.toString().substring(0, splitIndex));
		context.write(key, info);
	}
}

