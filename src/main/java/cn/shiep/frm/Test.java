package cn.shiep.frm;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

public class Test extends JFrame {
    private JTextArea textArea;
    private JButton openButton;
    private JFileChooser fileChooser;

    public Test() {
        setTitle("File Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);

        openButton = new JButton("Open File");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        fileChooser = new JFileChooser();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(openButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        add(panel);
    }

    private void showFileChooser() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            displayFileDetails(file);
        }
    }

    private void displayFileDetails(File file) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(file.getName()).append("\n");
        sb.append("Path: ").append(file.getAbsolutePath()).append("\n");
        sb.append("Size: ").append(file.length()).append(" bytes").append("\n");
        sb.append("Last Modified: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified())).append("\n");

        textArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Test window = new Test();
                window.setVisible(true);
            }
        });
    }
}






