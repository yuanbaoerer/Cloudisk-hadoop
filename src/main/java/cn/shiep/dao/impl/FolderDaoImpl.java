package cn.shiep.dao.impl;

import cn.shiep.dao.FolderDao;
import cn.shiep.eneity.File;
import cn.shiep.eneity.Folder;
import cn.shiep.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author yuanbao
 * @Date 2023/5/20
 * @Description
 */
public class FolderDaoImpl extends BaseDao {

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取自增列下一个id值
     * @return
     * @throws Exception
     */
    public int getAuto_increment() throws Exception{
        Connection connection = JDBCUtils.getConnection();
        String tableName = "folders";
        String sql = "show table status like '" + tableName + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        int auto_increment = 0;
        if (resultSet.next()){
            auto_increment = resultSet.getInt("Auto_increment");
            System.out.println(auto_increment);
        }
        return auto_increment;
    }


    /**
     * 统计某一文件夹下某一名字文件夹的数量，如果是零就表示可以新建这个名字的文件夹
     * 如果>0则按照同名数量添加后缀
     * @return
     */
    public int countFolderName(String folderName,int parentFolderID,int ownerID){
        String sql = "select count(*) from folders where folderName like concat('',?,'%') " +
                "and parentFolderID = ? and ownerID = ?";
        Number count = (Number) queryForSingleValue(sql, folderName, parentFolderID, ownerID);
        return count.intValue();
    }

    /**
     * 根据当前文件夹中的子文件夹名字都是唯一的来寻找
     * @param folderName
     * @return
     */
    public Folder queryFolderByFolderName(String folderName,Integer parentFolderID,Integer ownerID){
        String sql = "select * from folders where folderName = ? and parentFolderID = ? and ownerID = ?";
        return queryForOne(Folder.class,sql,folderName,parentFolderID,ownerID);
    }

    /**
     * 查询用户某文件夹下的所有子文件夹
     * @param ownerID
     * @param parentFolderID
     * @return
     */
    public List<Folder> queryFoldersByOwnerIDAndParentFolderID(Integer ownerID, Integer parentFolderID){
        String sql = "select * from folders where folderID != parentFolderID and ownerID = ? and parentFolderID  = ?";

        return queryForList(Folder.class,sql,ownerID,parentFolderID);
    }

    /**
     * 添加文件夹
     * @param folderName
     * @param parentFolderID
     * @param ownerID
     * @return
     */
    public int addFolder(String folderName,Integer parentFolderID,Integer ownerID) {
        String localDateTime = dtf.format(LocalDateTime.now());
        String sql = "insert into folders (folderName,parentFolderID,ownerID,creationTime,modificationTime) " +
                "values(?,?,?,?,?)";

        return update(sql,folderName,parentFolderID,ownerID,localDateTime,localDateTime);
    }

    public int deleteFolder(Integer folderID) {
        String sql = "delete from folders where folderID = ?";

        return update(sql,folderID);
    }

    public int updateFolderName(Integer folderID,String folderName_new) {
        String localDateTime = dtf.format(LocalDateTime.now());
        String sql = "update folders set folderName = ?,modificationTime = ? where folderID = ?";

        return update(sql,folderName_new,localDateTime,folderID);
    }

    public static void main(String[] args) throws Exception {


    }
}
