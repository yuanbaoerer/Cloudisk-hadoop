package cn.shiep.ex3.WordCount;

/**
 * @Author yuanbao
 * @Date 2023/3/31
 * @Description
 */
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 根据业务需求，只需要对mapper和reducer进行修改
 *
 * mapper是逐行读的，执行次数等于行数，每个单词都变成一个<key,value>，
 *
 * 相同的key经过中间环境是合并到一起的，合并到一个迭代器Iterable
 *
 * reducer是根据key读的，执行次数等于key
 *
 * 不存在相同的key存放在同一个reduce
 */
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    /**
     * @param key
     * @param value
     * @param context 上下文，Context是Mapper和Reducer与框架通信的接口，可以通过它来实现数据的输入和输出
     *                context.write(word,one)就是将键值对写入到MapReduce框架中，
     *                等待后续Reduce处理。具体来说，Context中的write()方法会将键值对写入到MapReduce框架
     *                的输出缓存中，缓存达到一定的大小后，就会写入到磁盘中。最终，输出数据会被写入到指定的输出路径中。
     * @throws IOException
     * @throws InterruptedException
     */
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            context.write(word, one);
        }
    }

    // private final static IntWritable one = new IntWritable(1);
    // private Text word = new Text();
    //
    // @Override
    // protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
    //     StringTokenizer itr = new StringTokenizer(value.toString());
    //     while (itr.hasMoreTokens()) {
    //         word.set(itr.nextToken());
    //         context.write(word, one);
    //     }
    // }
}
