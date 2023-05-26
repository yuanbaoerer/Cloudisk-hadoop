package cn.shiep.hdfs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

/**
 * @Author yuanbao
 * @Date 2023/3/17
 * @Description
 */
public class CopyFromHadoop {

    /**
     * 实现从hadoop复制文件到本地
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.18.128:9000");//套接字Socket

        FileSystem fs = FileSystem.get(conf);
        Path remotePath = new Path("/user/TestFile.txt");

        // Path localPath = new Path("file:///d:/HelloTest.java");
        //fs.copyFromLocalFile(false,true,localPath, remotePath);//是否删除源文件，是否覆盖目的文件
        //fs.copyToLocalFile(remotePath, localPath);

        FSDataInputStream in = fs.open(remotePath);
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = d.readLine()) != null) {
            System.out.println(line);
        }
        in.close();

        fs.close();
    }
}