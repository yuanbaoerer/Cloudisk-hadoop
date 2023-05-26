package cn.shiep.frm;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
import cn.shiep.dao.impl.UserDaoImpl;
import cn.shiep.eneity.User;
import cn.shiep.utils.JDBCUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrm() {
        setTitle("注册");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField(20);

        JButton registerButton = new JButton("注册");
        JButton cancelButton = new JButton("取消");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String userPwd = new String(passwordChars);
                User user = new User(null,userName,userPwd);
                // 在这里进行注册逻辑，根据需要自行编写

                UserDaoImpl userDao = new UserDaoImpl();
                try {
                    userDao.addUser(user);
                    JDBCUtils.commitAndClose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RegisterFrm.this, "用户名重复，请重试！");
                    throw new RuntimeException(ex);
                }

                JOptionPane.showMessageDialog(RegisterFrm.this, "注册成功！");
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        RegisterFrm.this.dispose();
                    }
                });
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegisterFrm();
            }
        });
    }
}

