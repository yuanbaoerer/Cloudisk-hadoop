package cn.shiep.frm;

import cn.shiep.dao.UserDao;
import cn.shiep.dao.impl.UserDaoImpl;
import cn.shiep.eneity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;


    public LoginFrm() {
        setTitle("登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String userPwd = new String(passwordChars);

                // 在这里进行登录验证逻辑，根据需要自行编写
                UserDaoImpl userDao = new UserDaoImpl();
                User user = userDao.queryUserByUserNameAndUserPwd(userName, userPwd);

                if (user != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            // MainFrm mainFrm = new MainFrm(user);
                        }
                    });
                    LoginFrm.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrm.this, "登录失败，请重试！");
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new RegisterFrm();
                    }
                });
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrm();
            }
        });
    }
}
