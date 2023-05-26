package cn.shiep.ex3.gran;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GranMap extends Mapper<Object, Text, Text, Text> {
	
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		String[] pcnames=new String[2];
		pcnames[0]=itr.nextToken();
		pcnames[1]=itr.nextToken();
		if(pcnames[0].compareTo("child")!=0) {
			context.write(new Text(pcnames[1]),new Text("1+"+pcnames[0]+"+"+pcnames[1]));
			context.write(new Text(pcnames[0]),new Text("2+"+pcnames[0]+"+"+pcnames[1]));
		}
		
	}
}