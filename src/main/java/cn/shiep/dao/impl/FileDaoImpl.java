package cn.shiep.dao.impl;

import cn.shiep.eneity.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class FileDaoImpl extends BaseDao{
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * （暂时未使用，目前重复文件的方式是覆盖）计算当前文件夹下同名文件的数量，以保证文件名不重复
     * @param fileName
     * @param folderID
     * @param ownerID
     * @return
     */
    public int countFileName(String fileName,int folderID,int ownerID){
        String sql = "select count(*) from files where fileName like concat('',?,'%') " +
                "and folderID = ? and ownerID = ?";
        Number count = (Number) queryForSingleValue(sql,fileName,folderID,ownerID);
        return count.intValue();
    }

    /**
     * 查询用户某文件夹下的所有文件
     * @param ownerID
     * @param FolderID
     * @return
     */
    public List<File> queryFileByOwnerIDAndFolderID(Integer ownerID,Integer FolderID){
        String sql = "select * from files where ownerID = ? and FolderID  = ? order by extension";

        return queryForList(File.class,sql,ownerID,FolderID);
    }

    /**
     * 搜索图片
     * @param ownerID
     * @return
     */
    public List<File> queryImage(Integer ownerID){
        String sql = "select * from files where ownerID = ? and extension = '.jpg'";

        return queryForList(File.class,sql,ownerID);
    }

    public List<File> queryDoc(Integer ownerID){
        String sql = "select * from files where ownerID = ? and extension = '.doc' or extension = '.docx'";

        return queryForList(File.class,sql,ownerID);
    }

    /**
     * 搜索音乐
     * @param ownerID
     * @return
     */
    public List<File> queryMusic(Integer ownerID){
        String sql = "select * from files where ownerID = ? and extension = '.mp3'";

        return queryForList(File.class,sql,ownerID);
    }

    /**
     * 搜索视频
     * @param ownerID
     * @return
     */
    public List<File> queryVideo(Integer ownerID){
        String sql = "select * from files where ownerID = ? and extension = '.mp4'";
        return queryForList(File.class,sql,ownerID);
    }

    /**
     * 搜索最近文件
     * @param ownerID
     * @return
     */
    public List<File> queryRecentFile(Integer ownerID){
        String sql = "select * from files where ownerID = ? order by operationTime desc";

        return queryForList(File.class,sql,ownerID);
    }

    /**
     * 模糊查询，应用于查询操作
     * @param ownerID
     * @param fileName
     * @return
     */
    public List<File> queryFileByOwnerIDAndFileName(Integer ownerID, String fileName){
        String sql = "select * from files where ownerID = ? and fileName like concat('%',?,'%') order by extension";
        return queryForList(File.class,sql,ownerID,fileName);
    }

    /**
     * 更新文件名，同时自动更新时间
     * @param fileID
     * @param fileName_new
     * @return
     */
    public int updateFileName(Integer fileID,String fileName_new) {
        String localDateTime = dtf.format(LocalDateTime.now());
        String sql = "update files set fileName = ?,modificationTime = ? where fileID = ?";

        return update(sql,fileName_new,localDateTime,fileID);
    }

    /**
     * 从hadoop中下载指定文件
     * @return
     */
    public File downloadFile(String fileName,Integer ownerID,Integer folderID){
        String fileRealName = ownerID+"_"+folderID+"_"+fileName;
        String sql = "select * from files where fileRealName = ?";
        return queryForOne(File.class,sql,fileRealName);
    }

    /**
     * 添加文件
     * @param fileName
     * @param filePath
     * @param fileSize
     * @param folderID
     * @param ownerID
     * @param extension
     * @param authority
     * @return
     */
    public int addFile(String fileName,String filePath,long fileSize,
                       Integer folderID,Integer ownerID,String extension,
                       String authority,String fileRealName){
        if (fileRealName == null || fileRealName.isEmpty()){
            fileRealName = ownerID+"_"+folderID+"_"+fileName;
        }
        String localDateTime = dtf.format(LocalDateTime.now());
        String sql = "insert into files (fileName,filePath,fileSize,creationTime," +
                "modificationTime,folderID,ownerID,extension,authority,fileRealName) values(" +
                "?,?,?,?,?,?,?,?,?,?)";

        return update(sql,fileName,filePath,fileSize,localDateTime,
                localDateTime,folderID,ownerID,extension,authority,fileRealName);
    }

    /**
     * 删除文件
     * @param fileID
     * @return
     */
    public int deleteFile(Integer fileID){
        String sql = "delete from files where fileID = ?";
        return update(sql,fileID);
    }
}
