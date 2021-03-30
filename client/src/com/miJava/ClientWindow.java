package com.miJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by on 29.03.2021.
 */
public class ClientWindow extends JFrame implements ActionListener, TCPConnoctionListener{

    private static final String IP_ADRESS = "localhost";
    private static final int PORT = 8080;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private TCPConnection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("alex");
    private final JTextField fieldInput = new JTextField();

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);
        fieldInput.addActionListener(this);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADRESS, PORT);
        } catch (IOException e) {
            printMsg("Connection Exception: ...." + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickName.getText() + ":" + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection Ready: ....");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String text) {
        printMsg(text);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection Close: ....");
    }

    @Override
    public void onExcepton(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection Exception: ....");
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
