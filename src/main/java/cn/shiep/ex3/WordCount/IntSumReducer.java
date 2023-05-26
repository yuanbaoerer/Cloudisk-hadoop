package cn.shiep.ex3.WordCount;

/**
 * @Author yuanbao
 * @Date 2023/3/31
 * @Description
 */
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
public class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        result.set(sum);
        context.write(key, result);
    }
    // @Override
    // protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
    //     int sum = 0;
    //     for (IntWritable value: values) {
    //         sum += value.get();
    //     }
    //     context.write(key, new IntWritable(sum));
    // }
}
