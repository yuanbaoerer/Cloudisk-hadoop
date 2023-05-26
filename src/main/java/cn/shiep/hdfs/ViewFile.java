package cn.shiep.hdfs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

/**
 * @Author yuanbao
 * @Date 2023/3/17
 * @Description
 */
public class ViewFile {

    /**
     * 判断路径是否存在
     */
    public static boolean test(Configuration conf, String path) {
        try (FileSystem fs = FileSystem.get(conf)) {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 追加文件内容
     */
    public static void appendToFile(Configuration conf, String localFilePath,
                                    String remoteFilePath) {
        Path remotePath = new Path(remoteFilePath);
        try {
            FileSystem fs = FileSystem.get(conf);
            FileInputStream in = new FileInputStream(localFilePath);
            FSDataOutputStream out = fs.append(remotePath);
            byte[] data = new byte[1024];
            int read = -1;
            while ((read = in.read(data)) > 0) {
                out.write(data, 0, read);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     */
    public static void view(Configuration conf, String remoteFilePath) {
        Path remotePath = new Path(remoteFilePath);
        try (FileSystem fs = FileSystem.get(conf);
             FSDataInputStream in = fs.open(remotePath);
             BufferedReader d = new BufferedReader(new InputStreamReader(in));) {
            String line;
            while ((line = d.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主函数
     */
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.18.128:9000");
        String remoteFilePath = "/user/TestFile.txt";// HDFS路径

        // try {
        //     System.out.println("读取文件: " + remoteFilePath);
        //     ViewFile.view(conf, remoteFilePath);
        //     System.out.println("\n读取完成");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        System.out.println(test(conf,"/user/20201439"));
    }
}
