package cn.shiep.dao.impl;

import cn.shiep.eneity.File;
import cn.shiep.utils.JDBCUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class FileDaoImplTest {

    FileDaoImpl fileDao = new FileDaoImpl();

    @Test
    public void addFile() {



    }

    @Test
    public void queryFileByOwnerIDAndFileName() {
        List<File> fi = fileDao.queryFileByOwnerIDAndFileName(6, "fi");
        System.out.println(fi);
    }

    @Test
    public void queryFileByOwnerIDAndParentFolderID() {
        List<File> files = fileDao.queryFileByOwnerIDAndFolderID(6, 12);
        System.out.println(files);
    }

    @Test
    public void updateFileName() {
        int i = fileDao.updateFileName(3, "fileTest3.txt");
        JDBCUtils.commitAndClose();
        System.out.println(i);
    }

    @Test
    public void downloadFile() {
        File file = fileDao.downloadFile("与自己对话.md", 6, 12);
        System.out.println(file);
    }

    @Test
    public void queryImage() {
        List<File> files = fileDao.queryImage(6);
        System.out.println(files);
    }

    @Test
    public void queryRecentFile() {
        List<File> files = fileDao.queryRecentFile(6);
        System.out.println(files);
    }

    @Test
    public void queryDoc() {
        List<File> files = fileDao.queryDoc(6);
        System.out.println(files);
    }

    @Test
    public void deleteFile() {
        int i = fileDao.deleteFile(11);
        JDBCUtils.commitAndClose();
        System.out.println(i);
    }
}