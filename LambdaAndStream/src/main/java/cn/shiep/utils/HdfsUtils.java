package cn.shiep.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;


/**
 * @Author yuanbao
 * @Date 2023/3/21
 * @Description
 */
public class HdfsUtils {
    static Configuration conf;
    public static FileSystem fs;
    static Path localPath;
    static Path remotePath;
    //默认
    private String hdfsHost = "hdfs://192.168.18.128:9000";

    public HdfsUtils() {
    }

    public HdfsUtils(String hdfsHost) {
        this.hdfsHost = hdfsHost;
    }

    /**
     * 配置文件
     */
    static {
        conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.18.128:9000");//套接字Socket
        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception{
        HdfsUtils hdfsUtils = new HdfsUtils();
        // hdfsUtils.copyFromHadoop("/user/TestFile.txt");
        // //测试show功能
        hdfsUtils.showFromHadoop("/Hello.sh");
        /**
         * 权限问题，通过关闭安全模式解决问题
         */
        //测试上传
        // hdfsUtils.copyToHadoop("file:///C:/myjava/AcWing算法基础课.md","/");
        //测试递归遍历显示指定文件夹下所有文件的信息
        // hdfsUtils.lsDir("/user/");
        /**
         * 问题为什么下载除了源文件，还多了.crc文件
         */
        //测试下载
        // hdfsUtils.copyFromHadoop("file:///C:/myjava/","/AcWing算法基础课.md");

        //测试追加文件内容
        // hdfsUtils.appendToFile("C:\\Users\\31031\\Desktop\\新建文本文档.txt","/Hello.sh");

        //测试判断目录是否存在
        // System.out.println(hdfsUtils.testDir("/"));


        // System.out.println(hdfsUtils.testDir("/ni11"));

        // hdfsUtils.copyToHadoop("file:///C:\\Users\\31031\\Desktop/AcWing算法基础课.md","/user/input/");
    }

    /**
     * 执行计数操作，如果输出目录已存在则删除输出目录后继续运行
     * @param inputFile
     * @param outputFile
     */
    public void exeCount(Job job,String inputFile,String outputFile) throws Exception {

        //设置输入和输出目录
        //指定输入路径
        // String inputFile = "hdfs://192.168.18.128:9000/user/input";
        FileInputFormat.addInputPath(job, new Path(inputFile));

        //指定输出路径
        // String outputFile = "hdfs://192.168.18.128:9000/user/output";
        if (testDir(outputFile)){
            deleteDir(outputFile);
            FileOutputFormat.setOutputPath(job, new Path(outputFile));
        }else {
            FileOutputFormat.setOutputPath(job, new Path(outputFile));
        }

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    /**
     * 创建目录
     * @param remoteDir
     */
    public void makeDir(String remoteDir) {
        try {
            Path dirPath = new Path(remoteDir);
            if (!fs.exists(dirPath)){
                fs.mkdirs(dirPath);
                System.out.println("创建目录成功");
            }else System.out.println("目录已存在");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录
     * @param remoteDir
     */
    public void deleteDir(String remoteDir){
        Path dirPath = new Path(remoteDir);
        try {
            if (fs.exists(dirPath)){
                //第一个参数是要删除的路径Path，第二个参数是是否递归
                fs.delete(dirPath,true);
                System.out.println("删除成功");
            }else {
                System.out.println("目录不存在，无法删除");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断路径是否存在
     * @param path
     * @return
     */
    public boolean testDir(String path) {
        try {
            if (fs.exists(new Path(path))){
                System.out.println(path+"目录存在");
                return true;
            }else {
                System.out.println(path+"目录不存在");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 追加文件内容
     * @param localFilePath
     * @param remoteFilePath
     */
    public void appendToFile(String localFilePath,String remoteFilePath) {
        remotePath = new Path(remoteFilePath);
        try {
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
     * 显示指定文件夹下所有文件的信息（递归）
     * @param remoteDir
     */
    public void lsDir(String remoteDir) {
        try {
            fs = FileSystem.get(conf);
            remotePath = new Path(remoteDir);
            /* 递归获取目录下的所有文件 */
            RemoteIterator<LocatedFileStatus> remoteIterator = fs.listFiles(remotePath, true);

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


    /**
     * 从hadoop读取文件文本内容并输出到控制台
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public void showFromHadoop(String remoteFilePath){
        remotePath = new Path(remoteFilePath);

        try {
            FSDataInputStream in = fs.open(remotePath);
            BufferedReader d = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = d.readLine()) != null) {
                System.out.println(line);
            }
            //关闭FSDataInputStream输入流
            in.close();
            //关闭FileSystem文件管理系统
            fs.close();
        } catch (IOException e) {
            System.out.println("读取失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * fs.copyToLocalFile()实现从hadoop下载文件功能
     * @param remoteFilePath 指定需要从hadoop下载的文件路径,eg:/user/TestFile.txt
     * @throws Exception
     */
    public void copyFromHadoop(String localFilePath,String remoteFilePath) {

        remotePath = new Path(remoteFilePath);
        localPath = new Path(localFilePath);

        try {
            fs.copyToLocalFile(remotePath, localPath);
            fs.close();
            System.out.println("下载成功");
            System.out.println("下载保存路径："+localFilePath);
        } catch (IOException e) {
            System.out.println("下载失败");
            throw new RuntimeException(e);
        }
    }


    /**
     * 实现从本地上传文件到hadoop功能
     * @param localFilePath 本地文件路径
     * @param remoteFilePath 上传路径
     * @throws Exception
     */
    public void copyToHadoop(String localFilePath,String remoteFilePath){
        remotePath = new Path(remoteFilePath);
        localPath = new Path(localFilePath);
        //是否删除源文件，是否覆盖目的文件

        try {
            fs.copyFromLocalFile(false,true,localPath,remotePath);
            fs.close();
            System.out.println("上传成功");
            System.out.println("上传路径："+remoteFilePath);
        } catch (IOException e) {
            System.out.println("上传失败");
            throw new RuntimeException(e);
        }
    }

}
