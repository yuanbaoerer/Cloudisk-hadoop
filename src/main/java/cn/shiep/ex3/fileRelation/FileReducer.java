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

/**   
*    
* ��Ŀ���ƣ�BigData   
* �����ƣ�FileReducer   
* ��������   
* �����ˣ�����ɽ   
* ����ʱ�䣺2023��4��10�� ����8:15:26   
* @version        
*/
public class FileReducer extends Reducer<Text, Text, Text, Text>{
	private static Text result = new Text(); 
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String fileList = new String();
		for (Text value : values) { 
			fileList += value.toString() + ";"; 
		} 
		result.set(fileList); 
		context.write(key, result); 
	} 
}
