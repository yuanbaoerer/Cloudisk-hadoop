package cn.shiep.frm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainFrm extends JFrame{
    private JTree tree=null;
    private JButton btnUpload = new JButton();
    private JButton btnDownload = new JButton();
    private JButton btnCreateDir = new JButton();
    private DefaultMutableTreeNode root=new DefaultMutableTreeNode("我的网盘");
    private JPanel jp_center=new JPanel();
    private void initTree() {
        tree=new JTree(root);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                tree_ValueChanged(e);
            }
        });

    }
    private void tree_ValueChanged(TreeSelectionEvent e) {
        JOptionPane.showMessageDialog(this,
                tree.getSelectionPath().getLastPathComponent().toString());
    }
    public MainFrm() {
        JPanel jp=(JPanel)this.getContentPane();
        initTree();
        JScrollPane  jsp_tree=new JScrollPane(tree);

        JPanel jp_top=new JPanel();
        jp_top.add(btnUpload);jp_top.add(btnDownload);jp_top.add(btnCreateDir);
        JSplitPane splitPane_right=new JSplitPane(JSplitPane.VERTICAL_SPLIT,jp_top,jp_center);
        splitPane_right.setDividerLocation(100);
        splitPane_right.setDividerSize(1);

        JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp_tree,splitPane_right);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(2);
        jp.add(splitPane);

        btnDownload.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDownload_Clicked();
            }
        });
        btnUpload.setBackground(Color.WHITE);
        btnUpload.setIcon(new  ImageIcon("src/Icons/upload.png"));
        btnUpload.setToolTipText("上传文件");
        btnDownload.setBackground(Color.WHITE);
        btnDownload.setIcon(new  ImageIcon("src/Icons/download.png"));
        btnDownload.setToolTipText("下载文件");
        btnCreateDir.setBackground(Color.WHITE);
        btnCreateDir.setIcon(new  ImageIcon("src/Icons/newdir.png"));
        btnCreateDir.setToolTipText("新建文件夹");
        this.setTitle("我的云盘");
        this.setSize(1200, 800);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void btnDownload_Clicked() {

    }
    private void btnUpload_Clicked() {

    }
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainFrm();
    }

}
