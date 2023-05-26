package cn.shiep.dao.impl;

import cn.shiep.dao.UserDao;
import cn.shiep.eneity.File;
import cn.shiep.eneity.Folder;
import cn.shiep.eneity.User;
import cn.shiep.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author yuanbao
 * @Date 2023/5/19
 * @Description
 */
public class UserDaoImpl extends BaseDao {
    public static void main(String[] args) throws Exception {
        // UserDaoImpl userDao = new UserDaoImpl();
    }


    public int getAuto_increment() throws Exception{
        Connection connection = JDBCUtils.getConnection();
        String tableName = "user";
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

    public Folder getUserRootFolder(Integer userID){
        String sql = "select * from folders where ownerID = ? and folderID = parentFolderID";

        return queryForOne(Folder.class,sql,userID);
    }

    /**
     * 注册新用户，如果创建user成功，则创建一个该userID下的根文件夹，
     * 根文件夹的folderID = parentFolderID
     * @param user
     * @return
     * @throws Exception
     */
    public int addUser(User user) throws Exception{

        String sql = "insert into user (userName,userPwd) " +
                "values(?,?)";
        int user_auto_increment = getAuto_increment();
        int update = update(sql, user.getUserName(), user.getUserPwd());
        //如果创建user成功，则创建一个该userID下的根文件夹，根文件夹的folderID = parentFolderID
        if (update > 0){
            FolderDaoImpl folderDao = new FolderDaoImpl();
            int folder_auto_increment = folderDao.getAuto_increment();
            folderDao.addFolder("我的网盘",folder_auto_increment,user_auto_increment);
            JDBCUtils.commitAndClose();
        }

        return update;
    }

    public User queryUserByUserNameAndUserPwd(String userName,String userPwd){
        String sql = "select * from user where userName = ? and userPwd = ?";

        return queryForOne(User.class,sql,userName,userPwd);
    }

}
