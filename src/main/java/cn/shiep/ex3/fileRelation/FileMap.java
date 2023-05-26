/**
 * 
 */
package cn.shiep.ex3.fileRelation;

/**   
*    
* ��Ŀ���ƣ�BigData   
* �����ƣ�FileMap   
* ��������   
* �����ˣ�����ɽ   
* ����ʱ�䣺2023��4��10�� ����8:15:13   
* @version        
*/
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
public class FileMap extends Mapper<Object, Text, Text, Text>{
	private static Text keyInfo = new Text(); // �洢���ʺ�URL���
	private static Text valueInfo = new Text(); // �洢��Ƶ
	private static FileSplit split; // �洢Split����
	// ʵ��map����
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException { 
		// ���<key,value>��������FileSplit����
		split = (FileSplit) context.getInputSplit(); 
		StringTokenizer itr = new StringTokenizer(value.toString()); 
		while (itr.hasMoreTokens()) { 

			int splitIndex = split.getPath().toString().indexOf("file"); 
			keyInfo.set(itr.nextToken() + ":" + split.getPath().toString().substring(splitIndex)); 
			valueInfo.set("1");
			context.write(keyInfo, valueInfo); 
		} 
	} 
}
