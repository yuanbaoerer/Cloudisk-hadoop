package cn.shiep.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * @Author yuanbao
 * @Date 2023/3/17
 * @Description
 */
public class ListDir {

    /**
     * 创建目录
     */
    public static void makeDir(Configuration conf, String remoteDir) {
        try {
            FileSystem fs = FileSystem.get(conf);
            Path dirPath = new Path(remoteDir);
            System.out.println(fs.exists(dirPath));   //.mkdirs(dirPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示指定文件夹下所有文件的信息（递归）
     */
    public static void lsDir(Configuration conf, String remoteDir) {
        try {
            FileSystem fs = FileSystem.get(conf);
            Path dirPath = new Path(remoteDir);
            /* 递归获取目录下的所有文件 */
            RemoteIterator<LocatedFileStatus> remoteIterator = fs.listFiles(dirPath, true);
			/*RemoteIterator<LocatedFileStatus> remoteIterator=fs.listLocatedStatus(dirPath);
			while(remoteIterator.hasNext()) {
				LocatedFileStatus file=remoteIterator.next();
				if(file.isDirectory())
					System.out.println(file.getPath());
			}*/
            //输出每个文件的信息
            while (remoteIterator.hasNext()) {
                FileStatus s = remoteIterator.next();
                System.out.println("路径: " + s.getPath().toString());
                System.out.println("权限: " + s.getPermission().toString());
                System.out.println("大小: " + s.getLen());
                //返回的是时间戳,转化为时间日期格式
                Long timeStamp = s.getModificationTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(timeStamp);
                System.out.println("时间: " + date);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.18.128:9000");
        String remoteDir = "/mydir"; // HDFS路径
        //makeDir(conf,remoteDir);
        try {
            System.out.println("(递归)读取目录下所有文件的信息: " + remoteDir);
            ListDir.lsDir(conf, remoteDir);
            System.out.println("读取完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
