package cn.shiep.frm;

import cn.shiep.dao.impl.FileDaoImpl;
import cn.shiep.dao.impl.FolderDaoImpl;
import cn.shiep.dao.impl.UserDaoImpl;
import cn.shiep.eneity.File;
import cn.shiep.eneity.Folder;
import cn.shiep.eneity.User;
import cn.shiep.hdfsUtils.HdfsUtils;
import cn.shiep.utils.JDBCUtils;
import jdk.nashorn.internal.scripts.JO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrm extends JFrame{

    HdfsUtils hdfsUtils = new HdfsUtils();

    static Configuration conf;
    static {
        conf = new Configuration();
        conf.set("fs.FS", "hdfs://192.168.18.128:9000");//套接字Socket
    }
    private JTree tree=null;
    private JButton btnUpload = new JButton("上传文件",new ImageIcon("src/main/Icons/upload.png"));
    private JButton btnDownload = new JButton("下载文件",new ImageIcon("src/main/Icons/download.png"));
    private JButton btnCreateDir = new JButton("新建文件夹",new ImageIcon("src/main/Icons/newdir.png"));
    private JButton btnBack = new JButton("<返回");
    private JButton btnNext = new JButton("前进>");
    private JButton btnBackRoot = new JButton("我的网盘 > ");
    private DefaultMutableTreeNode root=new DefaultMutableTreeNode("我的网盘");
    private DefaultMutableTreeNode root_Recent=new DefaultMutableTreeNode("最近");
    private DefaultMutableTreeNode root_Image=new DefaultMutableTreeNode("图片");
    private DefaultMutableTreeNode root_Video=new DefaultMutableTreeNode("视频");
    private DefaultMutableTreeNode root_Doc=new DefaultMutableTreeNode("文档");
    private DefaultMutableTreeNode root_Music=new DefaultMutableTreeNode("音乐");

    //这个是右下角那一大片区域
    private JPanel jp_center=new JPanel();
    private JPanel jPanel_directory = new JPanel();
    JPanel jp_center_center;

    JTextField jTextField = new JTextField(15);
    JButton btnSearch = new JButton("搜索");

    //存放登录时获取的用户信息
    private User user;
    //存放从数据库中读取到的根目录中地文件信息
    private List<File> rootFileList;
    //存放从数据库中读取到的根目录中文件夹信息
    private List<Folder> rootFolderList;

    private List<File> fileList;
    private List<Folder> folderList;
    //登录时初始化的根文件夹
    private Folder rootFolder;
    //当前文件夹
    private Folder nowFolder;
    private Folder lastFolder;
    private Folder nextFolder;

    //目录链表
    private List<Folder> directoryList = new ArrayList<>();
    private int directoryIndex = 0;

    private Object clipboard = null;

    private FileDaoImpl fileDao = new FileDaoImpl();
    private FolderDaoImpl folderDao = new FolderDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();


    public MainFrm() {
        //-----------------数据初始化
        this.user = userDao.queryUserByUserNameAndUserPwd("admin4","123456");
        rootFolder = userDao.getUserRootFolder(user.getId());
        nowFolder = new Folder(rootFolder.getFolderID(),rootFolder.getFolderName(),rootFolder.getParentFolderID(),
                rootFolder.getCreationTime(),rootFolder.getOwnerID(),rootFolder.getModificationTime());
        lastFolder = new Folder(rootFolder.getFolderID(),rootFolder.getFolderName(),rootFolder.getParentFolderID(),
                rootFolder.getCreationTime(),rootFolder.getOwnerID(),rootFolder.getModificationTime());
        rootFolderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),rootFolder.getFolderID());
        rootFileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(),rootFolder.getFolderID());

        JPanel jp=(JPanel)this.getContentPane();
        initTree();

        //用于在需要滚动情况下包装其他组件
        JScrollPane jsp_tree=new JScrollPane(tree);

        //--------------------右上角部分-------------------
        JPanel jp_top=new JPanel();

        jp_top.add(btnUpload);jp_top.add(btnDownload);jp_top.add(btnCreateDir);
        jp_top.add(jTextField);jp_top.add(btnSearch);

        //-----------------------------右下角部分-----------------
        //添加滚动panel到右下角那部分
        jp_center_center = new JPanel();
        JScrollPane jsp_center = new JScrollPane(jp_center_center);
        jsp_center.setPreferredSize(new Dimension(800,600));
        //rows设置为0表示行数是自动填充，而cols是固定为7
        jp_center_center.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp_center_center.setPreferredSize(new Dimension(700,550));

        //垂直分割，目录显示和文件块部分
        jPanel_directory.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel_directory.add(btnBack);
        jPanel_directory.add(btnNext);
        jPanel_directory.add(btnBackRoot);
        JSplitPane jp_center_jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,jPanel_directory,jsp_center);
        jp_center_jSplitPane.setDividerLocation(50);
        jp_center_jSplitPane.setDividerSize(1);

        jp_center.add(jp_center_jSplitPane);


        //-----------------------------
        //垂直分割
        JSplitPane splitPane_right=new JSplitPane(JSplitPane.VERTICAL_SPLIT,jp_top,jp_center);
        splitPane_right.setDividerLocation(100);
        //设置分隔条的大小
        splitPane_right.setDividerSize(1);

        //水平分割
        JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp_tree,splitPane_right);
        splitPane.setDividerLocation(150);
        splitPane.setDividerSize(2);
        jp.add(splitPane);

        /**
         * 初始化根页面
         */
        initJPanel(rootFolderList,rootFileList);


        /**
         * 返回根目录按钮
         */
        btnBackRoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnBackRoot(rootFolder);
            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnBack_Clicked(lastFolder);
            }
        });
        btnCreateDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCreateDir(nowFolder);
            }
        });
        btnUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnUpload(nowFolder);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = jTextField.getText();
                if (input != null || !input.equals("")){
                    btnSearch(input);
                }
            }
        });



        this.setTitle(user.getUserName()+"的云盘");
        this.setSize(1200, 800);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 初始化每一个文件夹按钮，宽度100，高度75，保持图片在上，文字在下
     * @param folderName
     * @param imageIconPath
     * @return
     */
    private JButton initJButton(String folderName,String imageIconPath){
        JButton jButton = new JButton(folderName,new ImageIcon(imageIconPath));
        jButton.setPreferredSize(new Dimension(100,100));
        jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton.setHorizontalTextPosition(SwingConstants.CENTER);

        //右击菜单位置
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem_open = new JMenuItem("打开");
        JMenuItem menuItem_download = new JMenuItem("下载");
        JMenuItem menuItem_copy = new JMenuItem("复制");
        JMenuItem menuItem_shear = new JMenuItem("剪切");
        JMenuItem menuItem_move = new JMenuItem("移动到");
        JMenuItem menuItem_rename = new JMenuItem("重命名");
        JMenuItem menuItem_detials = new JMenuItem("详细信息");
        JMenuItem menuItem_delete = new JMenuItem("删除");
        popupMenu.add(menuItem_open);
        popupMenu.add(menuItem_download);
        popupMenu.add(menuItem_copy);
        popupMenu.add(menuItem_shear);
        popupMenu.add(menuItem_move);
        popupMenu.add(menuItem_rename);
        popupMenu.add(menuItem_detials);
        popupMenu.add(menuItem_delete);

        jButton.setComponentPopupMenu(popupMenu);

        return jButton;
    }

    /**
     * 将刷新文件夹和文件页面封装
     * @param folderList
     * @param fileList
     */
    private void initJPanel(List<Folder> folderList,List<File> fileList){
        jp_center_center.removeAll();
        jPanel_directory.removeAll();
        jPanel_directory.add(btnBack);
        jPanel_directory.add(btnNext);
        jPanel_directory.add(btnBackRoot);

        for (int i = 0;i<directoryList.size();i++){
            Folder folder = directoryList.get(i);
            JButton jButton = new JButton(folder.getFolderName() + ">");
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnFolder_Clicked(folder);
                }
            });
            jPanel_directory.add(jButton);
        }

        JPopupMenu popupMenuJPanel = new JPopupMenu();
        JMenuItem menuItemJPanel_upload = new JMenuItem("上传");
        JMenuItem menuItemJPanel_CreateDir = new JMenuItem("新建文件夹");
        JMenuItem menuItemJPanel_paste = new JMenuItem("粘贴");
        popupMenuJPanel.add(menuItemJPanel_upload);
        popupMenuJPanel.add(menuItemJPanel_CreateDir);
        //只有当clipboard不为空时，才添加粘贴按钮
        if (clipboard != null){
            popupMenuJPanel.add(menuItemJPanel_paste);
        }
        jp_center_center.setComponentPopupMenu(popupMenuJPanel);

        menuItemJPanel_upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnUpload(nowFolder);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        menuItemJPanel_CreateDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCreateDir(nowFolder);
            }
        });

        menuItemJPanel_paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPaste(nowFolder);
            }
        });


        if (folderList != null){
            for(Folder folder1 : folderList){
                JButton jButton = initJButton(folder1.getFolderName(), String.valueOf(Icons.NEWDIR.getSrc()));
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jp_center_center.removeAll();
                        btnFolder_Clicked(folder1);
                        jp_center_center.revalidate();
                        jp_center_center.repaint();
                    }
                });
                JPopupMenu popupMenu = jButton.getComponentPopupMenu();
                JMenuItem menuItem_open  = (JMenuItem) popupMenu.getComponent(0);
                JMenuItem menuItem_download  = (JMenuItem) popupMenu.getComponent(1);
                JMenuItem menuItem_copy  = (JMenuItem) popupMenu.getComponent(2);
                JMenuItem menuItem_shear  = (JMenuItem) popupMenu.getComponent(3);
                JMenuItem menuItem_move  = (JMenuItem) popupMenu.getComponent(4);
                JMenuItem menuItem_rename  = (JMenuItem) popupMenu.getComponent(5);
                JMenuItem menuItem_details  = (JMenuItem) popupMenu.getComponent(6);
                JMenuItem menuItem_delete  = (JMenuItem) popupMenu.getComponent(7);
                /**
                 * 打开文件夹
                 */
                menuItem_open.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jp_center_center.removeAll();
                        btnFolder_Clicked(folder1);
                        jp_center_center.revalidate();
                        jp_center_center.repaint();
                    }
                });

                /**
                 * 右击菜单的下载
                 */
                menuItem_download.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                /**
                 * 右击菜单的复制
                 */
                menuItem_copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });

                /**
                 * 右击菜单的剪切
                 */
                menuItem_shear.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                /**
                 * 右击菜单的移动到
                 */
                menuItem_move.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });

                /**
                 * 右击菜单的重命名
                 */
                menuItem_rename.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnFolderRename(folder1);
                        jp_center_center.removeAll();
                        btnFolder_Clicked(nowFolder);
                        jp_center_center.revalidate();
                        jp_center_center.repaint();
                    }
                });

                /**
                 * 右击菜单详细信息
                 */
                menuItem_details.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });

                /**
                 * 右击菜单的删除
                 */
                menuItem_delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnDeleteFolder(folder1);
                        jp_center_center.removeAll();
                        btnFolder_Clicked(nowFolder);
                        jp_center_center.revalidate();
                        jp_center_center.repaint();
                    }
                });

                jp_center_center.add(jButton);
            }
        }

        if (fileList != null){
            for (File file : fileList){
                String src = null;
                String fileExtension = file.getExtension();
                switch (fileExtension){
                    case ".txt":
                        src = String.valueOf(Icons.TXT.getSrc());
                        break;
                    case ".docx":
                    case ".doc":
                        src = String.valueOf(Icons.DOC.getSrc());
                        break;
                    case ".mp3":
                        src = String.valueOf(Icons.MUSIC.getSrc());
                        break;
                    case ".zip":
                    case ".tar":
                        src = String.valueOf(Icons.ZIP.getSrc());
                        break;
                    case ".xls":
                    case ".xlsx":
                        src = String.valueOf(Icons.EXCEL.getSrc());
                        break;
                    case ".ppt":
                    case ".pptx":
                        src = String.valueOf(Icons.PPT.getSrc());
                        break;
                    case ".mp4":
                        src = String.valueOf(Icons.VIDEO.getSrc());
                        break;
                    case ".jpg":
                        src = src = String.valueOf(Icons.IMAGE.getSrc());
                        break;
                    default:
                        src = String.valueOf(Icons.NONE.getSrc());
                        break;
                }
                JButton jButton = initJButton(file.getFileName(), src);
                JPopupMenu popupMenuFile = jButton.getComponentPopupMenu();
                JMenuItem menuItem_openFile  = (JMenuItem) popupMenuFile.getComponent(0);
                JMenuItem menuItem_downloadFile  = (JMenuItem) popupMenuFile.getComponent(1);
                JMenuItem menuItem_copy  = (JMenuItem) popupMenuFile.getComponent(2);
                JMenuItem menuItem_shear  = (JMenuItem) popupMenuFile.getComponent(3);
                JMenuItem menuItem_move  = (JMenuItem) popupMenuFile.getComponent(4);
                JMenuItem menuItem_rename  = (JMenuItem) popupMenuFile.getComponent(5);
                JMenuItem menuItem_detailsFile  = (JMenuItem) popupMenuFile.getComponent(6);
                JMenuItem menuItem_deleteFile  = (JMenuItem) popupMenuFile.getComponent(7);

                menuItem_openFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            btnFile_Open(file);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                menuItem_downloadFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            btnDownloadFile_Clicked(file);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                menuItem_copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCopyFile(file);
                    }
                });

                menuItem_rename.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnFileRename(file);
                        btnFolder_Clicked(nowFolder);
                    }
                });

                menuItem_detailsFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showFileDetailInfo(file);
                    }
                });

                menuItem_deleteFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnDeleteFile(file);
                        btnFolder_Clicked(nowFolder);
                    }
                });


                jp_center_center.add(jButton);
            }
        }

        jp_center_center.revalidate();
        jp_center_center.repaint();
        jPanel_directory.revalidate();
        jPanel_directory.repaint();
    }

    /**
     * 文件复制
     * 只涉及到数据库内的file，如果是粘贴到当前目录，则需要更新文件名
     * 如果是粘贴到别的目录，则需要插入一条文件数据，把folderID改一下
     */
    private void btnCopyFile(File file){
        //将需要复制的文件赋值给剪贴板
        clipboard = file;
        folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),file.getFolderID());
        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(),file.getFolderID());
        initJPanel(folderList,fileList);
    }

    /**
     * 粘贴操作
     */
    private void btnPaste(Folder folder){
        if (clipboard instanceof File){
            File file = (File) clipboard;
            String fileName = file.getFileName();
            long fileSize = file.getFileSize();
            String fileExtension = file.getExtension();
            String authority = file.getAuthority();
            int count = fileDao.countFileName(file.getFileName(),folder.getFolderID(),user.getId());
            if (count == 0){
                fileDao.addFile(fileName,"/cloudisk",fileSize,folder.getFolderID(),
                        user.getId(),fileExtension,authority,file.getFileRealName());
                JDBCUtils.commitAndClose();
            }else {
                String input = fileName+"（"+count+"）";
                fileDao.updateFileName(file.getFileID(),input);
                JDBCUtils.commitAndClose();
            }
        }
        folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),nowFolder.getFolderID());
        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(),nowFolder.getFolderID());
        initJPanel(folderList,fileList);
    }

    /**
     * 搜索
     * @param fileNameLike
     */
    private void btnSearch(String fileNameLike){
        fileList = fileDao.queryFileByOwnerIDAndFileName(user.getId(),fileNameLike);
        initJPanel(null,fileList);
    }

    /**
     * 打开文件，显示文件内容，目前txt和md可以正常输出，别的如doc会出乱码
     * @param file
     */
    private void btnFile_Open(File file) throws Exception{
        String extension = file.getExtension();
        switch (extension){
            case ".txt":
            case ".md":
                JFrame jFrame = new JFrame(file.getFileName());
                JTextPane jTextPane = new JTextPane();
                jTextPane.setEditable(false);
                JScrollPane jScrollPane = new JScrollPane(jTextPane);
                jFrame.add(jScrollPane);

                String fileRealPath = file.getFilePath()+"/"+file.getFileRealName();

                StringBuilder context = hdfsUtils.showFromHadoop(fileRealPath);
                jTextPane.setText(context.toString());
                jFrame.setSize(450,600);
                jFrame.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this,"暂不支持打开该文件类型");
        }

    }

    /**
     * 文件夹点击事件，打开被点击的文件夹
     * @param folder
     */
    private void btnFolder_Clicked(Folder folder){
        lastFolder = new Folder(nowFolder.getFolderID(), nowFolder.getFolderName(),nowFolder.getParentFolderID(),
                nowFolder.getCreationTime(),nowFolder.getOwnerID(),nowFolder.getModificationTime());
        nowFolder = folderDao.queryFolderByFolderName(folder.getFolderName(),folder.getParentFolderID(),folder.getOwnerID());

        folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),nowFolder.getFolderID());

        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(), folder.getFolderID());

        directoryList.add(folder);
        initJPanel(folderList,fileList);
    }


    /**
     * 返回到根目录事件
     */
    private void btnBackRoot(Folder folder){
        lastFolder = new Folder(nowFolder.getFolderID(), nowFolder.getFolderName(),nowFolder.getParentFolderID(),
                nowFolder.getCreationTime(),nowFolder.getOwnerID(),nowFolder.getModificationTime());
        nowFolder = folderDao.queryFolderByFolderName(folder.getFolderName(),folder.getParentFolderID(),folder.getOwnerID());
        rootFolderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),rootFolder.getFolderID());
        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(), folder.getFolderID());

        initJPanel(rootFolderList,fileList);
    }

    /**
     * 返回到上一级目录
     */
    private void btnBack_Clicked(Folder folder){
        lastFolder = new Folder(nowFolder.getFolderID(), nowFolder.getFolderName(),nowFolder.getParentFolderID(),
                nowFolder.getCreationTime(),nowFolder.getOwnerID(),nowFolder.getModificationTime());
        nowFolder = folderDao.queryFolderByFolderName(folder.getFolderName(),folder.getParentFolderID(),folder.getOwnerID());
        folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(), lastFolder.getParentFolderID());
        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(), folder.getFolderID());
        directoryList.remove(directoryList.size()-1);
        initJPanel(folderList,fileList);
    }

    /**
     * 上传文件
     */
    private void btnUpload(Folder folder) throws IOException {
        JFrame frame = new JFrame("File Chooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * 点击上传文件，会打开系统文件选择界面
         */
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            String selectedFilePath = fileChooser.getSelectedFile().getPath();
            long fileSize = fileChooser.getSelectedFile().length();
            int slashIndex = selectedFilePath.lastIndexOf("\\");
            int pointIndex = selectedFilePath.lastIndexOf(".");
            String fileName = selectedFilePath.substring(slashIndex+1);
            String fileExtension = selectedFilePath.substring(pointIndex);
            String authority = "rw-r--r--";
            System.out.println("Selected File:"+selectedFilePath);
            System.out.println(fileName);
            System.out.println(fileExtension);
            System.out.println("文件大小为："+fileSize+" KB");
            /**
             * 上传同名文件，hadoop中会覆盖，数据库中也要更新数据
             */
            hdfsUtils.copyToHadoop(selectedFilePath,"/cloudisk/"+
                    user.getId()+"_"+folder.getFolderID()+"_"+fileName);
            fileDao.addFile(fileName,"/cloudisk",fileSize,folder.getFolderID(),
                    user.getId(),fileExtension,authority,null);
            JDBCUtils.commitAndClose();


            folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),folder.getFolderID());
            fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(),folder.getFolderID());
            initJPanel(folderList,fileList);
        }
    }

    /**
     * 下载文件
     */
    private void btnDownloadFile_Clicked(File file) {
        JFrame frame = new JFrame("设置下载存储路径");
        String input = JOptionPane.showInputDialog(frame,"下载到：");
        String fileRealPath = file.getFilePath()+"/"+file.getFileRealName();
        if (input != null && !input.isEmpty()){
            try {
                hdfsUtils.copyFromHadoop(input,fileRealPath);
                JOptionPane.showMessageDialog(this,"下载成功");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,"下载失败");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 文件夹重命名
     * @param folder
     */
    private void btnFolderRename(Folder folder){
        JFrame frame = new JFrame("请输入新名称");
        String input = JOptionPane.showInputDialog(frame,"请输入新名称：");
        if (input != null && !input.isEmpty()){
            int count = folderDao.countFolderName(input, nowFolder.getFolderID(), user.getId());
            if (count == 0){
                folderDao.updateFolderName(folder.getFolderID(), input);
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"修改成功");
            }else {
                input = input+"（"+count+"）";
                folderDao.updateFolderName(folder.getFolderID(), input);
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"修改成功");
            }
        }
    }

    private void btnFileRename(File file){
        JFrame frame = new JFrame("请输入新名称：");
        String input = JOptionPane.showInputDialog(frame,"请输入新名称：");
        if (input != null && !input.isEmpty()){
            int count = fileDao.countFileName(file.getFileName(),file.getFolderID(),user.getId());
            if (count == 0){
                fileDao.updateFileName(file.getFileID(),input);
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"修改成功");
            }else {
                input = input+"（"+count+"）";
                fileDao.updateFileName(file.getFileID(),input);
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"修改成功");
            }
        }
    }

    /**
     * 显示文件详细信息
     * @param file
     */
    private void showFileDetailInfo(File file){
        JFrame jFrame = new JFrame("详细信息");
        jFrame.setVisible(true);
        jFrame.setSize(400,300);
        JTextArea jTextArea = new JTextArea();
        String fileName = file.getFileName();
        long fileSize = file.getFileSize();
        String extension = file.getExtension();
        String modification = file.getmodificationTime();

        StringBuilder context = new StringBuilder();
        context.append(fileName).append("\n");
        context.append("大小：").append(fileSize).append("byte").append("\n");
        context.append("类型：").append(extension).append("\n");
        context.append("修改时间：").append(modification).append("\n");
        jTextArea.setText(context.toString());
        jTextArea.setEditable(false);

        jFrame.add(jTextArea);
    }

    /**
     * 新建文件夹，同时更新文件夹更新时间
     */
    private void btnCreateDir(Folder nowFolder){
        JFrame frame = new JFrame("请输入文件夹名称");
        String input = JOptionPane.showInputDialog(frame,"请输入文件夹名称：");
        if (input != null && !input.isEmpty()){
            int count = folderDao.countFolderName(input, nowFolder.getFolderID(), user.getId());
            if (count == 0){
                folderDao.addFolder(input,nowFolder.getFolderID(),user.getId());
                folderDao.updateFolderName(nowFolder.getFolderID(), "");
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"创建成功");
            }else {
                folderDao.addFolder(input+"（"+count+"）",nowFolder.getFolderID(),user.getId());
                folderDao.updateFolderName(nowFolder.getFolderID(), "");
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(frame,"创建成功");
            }
        }else {
            JOptionPane.showMessageDialog(frame,"文件名不能为空");
        }
        folderList = folderDao.queryFoldersByOwnerIDAndParentFolderID(user.getId(),nowFolder.getFolderID());
        fileList = fileDao.queryFileByOwnerIDAndFolderID(user.getId(),nowFolder.getFolderID());
        initJPanel(folderList,fileList);
    }

    /**
     * 删除文件
     */
    private void btnDeleteFile(File file){
        JFrame jFrame = new JFrame("删除警告");
        int result = JOptionPane.showConfirmDialog(jFrame,"是否确认删除"+file.getFileName());
        if (result == JOptionPane.YES_NO_OPTION){
            if (fileDao.deleteFile(file.getFileID()) > 0){
                JDBCUtils.commitAndClose();
                String fileRealPath = file.getFilePath()+"/"+file.getFileRealName();
                try {
                    hdfsUtils.deleteDir(fileRealPath);
                    JOptionPane.showMessageDialog(this,"删除成功");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                JOptionPane.showMessageDialog(this,"删除失败");
            }
        }
    }

    /**
     * 删除文件夹
     * @param folder
     */
    private void btnDeleteFolder(Folder folder){
        JFrame jFrame = new JFrame("删除警告");
        int result = JOptionPane.showConfirmDialog(jFrame,"是否确认删除"+folder.getFolderName());
        if (result == JOptionPane.YES_NO_OPTION){
            if (folderDao.deleteFolder(folder.getFolderID()) > 0){
                JDBCUtils.commitAndClose();
                JOptionPane.showMessageDialog(this,"删除成功");
            }else {
                JOptionPane.showMessageDialog(this,"删除失败");
            }
        }
    }

    private void initTree() {
        tree=new JTree(root);
        root.add(root_Recent);
        root.add(root_Image);
        root.add(root_Video);
        root.add(root_Doc);
        root.add(root_Music);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                tree_ValueChanged(e);
            }
        });

    }


    private void tree_ValueChanged(TreeSelectionEvent e) {
        String select = tree.getSelectionPath().getLastPathComponent().toString();
        switch (select){
            case "我的网盘":
                btnBackRoot(rootFolder);
                break;
            case "最近":
                fileList = fileDao.queryRecentFile(user.getId());
                initJPanel(null,fileList);
                break;
            case "图片":
                fileList = fileDao.queryImage(user.getId());
                initJPanel(null,fileList);
                break;
            case "视频":
                fileList = fileDao.queryVideo(user.getId());
                initJPanel(null,fileList);
                break;
            case "文档":
                fileList = fileDao.queryDoc(user.getId());
                initJPanel(null,fileList);
                break;
            case "音乐":
                fileList = fileDao.queryMusic(user.getId());
                initJPanel(null,fileList);
                break;
        }
    }



    public static void main(String[] args) {
        new MainFrm();

    }
}


