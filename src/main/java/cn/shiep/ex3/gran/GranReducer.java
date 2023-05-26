
package cn.shiep.ex3.gran;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer.Context;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class GranReducer extends Reducer<Text, Text, Text, Text> {
	public static int time = 0;

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		if (0 == time) {
			context.write(new Text("grandchild"), new Text("grandparent"));
			time++;
		}
		int grandchildnum = 0;
		String[] grandchild = new String[10];
		int grandparentnum = 0;
		String[] grandparent = new String[10];
		Iterator<Text> ite = values.iterator();
		while (ite.hasNext()) {
			String record = ite.next().toString();
			int len = record.length();
			int i = 2;
			if (0 == len) {
				continue;
			}
			char relationtype = record.charAt(0);
			String childname = new String();
			String parentname = new String();
			while (record.charAt(i) != '+') {
				childname += record.charAt(i);
				i++;
			}
			i = i + 1;
			while (i < len) { // 1 childname+parentname
				parentname += record.charAt(i);
				i++;
			}
			if ('1' == relationtype) {
				grandchild[grandchildnum] = childname;
				grandchildnum++;
			}
			if ('2' == relationtype) {
				grandparent[grandparentnum] = parentname;
				grandparentnum++;
			}
		}
		if (0 != grandchildnum && 0 != grandparentnum) {
			for (int m = 0; m < grandchildnum; m++) {
				for (int n = 0; n < grandparentnum; n++) {
					context.write(new Text(grandchild[m]), new Text(grandparent[n]));
				}
			}
		}
	}
}