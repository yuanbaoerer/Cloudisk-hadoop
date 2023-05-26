package cn.shiep.ex3.childParent;

import cn.shiep.conf.Conf;
import cn.shiep.hdfsUtils.HdfsUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @Author yuanbao
 * @Date 2023/4/23
 * @Description
 */
public class childParent {

    public static void main(String[] args) throws Exception {
        //这里不能使用combine不然reduce没有输出
        Conf conf = new Conf("ChildParent",childParent.class,Map.class,Reduce.class,Text.class,Text.class);
        HdfsUtils hdfsUtils = new HdfsUtils();
        hdfsUtils.exeCount(conf.getJob(),"hdfs://192.168.18.128:9000/user/ex3/STjoin/input/child-parent.txt","hdfs://192.168.18.128:9000/user/ex3/STjoin/output/");

    }
}
