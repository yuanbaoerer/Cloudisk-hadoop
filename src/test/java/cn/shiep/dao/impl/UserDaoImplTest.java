package cn.shiep.dao.impl;

import cn.shiep.eneity.File;
import cn.shiep.eneity.Folder;
import cn.shiep.eneity.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class UserDaoImplTest {

    UserDaoImpl userDao = new UserDaoImpl();

    @Test
    public void addUser() throws Exception {
        int admin4 = userDao.addUser(new User(null, "admin8", "123456"));
        System.out.println(admin4);
    }

    @Test
    public void queryUserByUserNameAndUserPwd() {
        User admin4 = userDao.queryUserByUserNameAndUserPwd("admin4", "123456");
        System.out.println(admin4);
    }

    @Test
    public void getUserRootFolderID() {
        Folder userRootFolderID = userDao.getUserRootFolder(6);
        System.out.println(userRootFolderID);
    }
}