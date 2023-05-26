package cn.shiep.dao.impl;

import cn.shiep.eneity.Folder;
import cn.shiep.utils.JDBCUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author yuanbao
 * @Date 2023/5/20
 * @Description
 */
public class FolderDaoImplTest {

    FolderDaoImpl folderDao = new FolderDaoImpl();

    @Test
    public void addFolder() throws Exception {


        System.out.println(folderDao.addFolder("Test5_Test1",12,6));
        JDBCUtils.commitAndClose();
    }

    @Test
    public void deleteFolder() {
        folderDao.deleteFolder(10);
        JDBCUtils.commitAndClose();
    }

    @Test
    public void updateFolderName() {
        folderDao.updateFolderName(9,"Test3_new_new");
        JDBCUtils.commitAndClose();
    }

    @Test
    public void queryFoldersByOwnerIDAndParentFolderID() {
        List<Folder> folders = folderDao.queryFoldersByOwnerIDAndParentFolderID(2, 5);
        System.out.println(folders);
    }

    @Test
    public void queryFolderByFolderName() {
        Folder folder = folderDao.queryFolderByFolderName("Test4", 12, 6);
        System.out.println(folder);
    }

    @Test
    public void countFolderName() {
        int test = folderDao.countFolderName("Test", 12, 6);
        System.out.println(test);
    }
}