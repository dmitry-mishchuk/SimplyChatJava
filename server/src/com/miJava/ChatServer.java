package com.miJava;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by on 29.03.2021.
 */
public class ChatServer implements TCPConnoctionListener{
    public static void main(String[] args) {
        new ChatServer();
    }
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("server running....");
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            while(true){
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConnection exeption ...." + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection.toString());
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String text) {
        sendToAllConnections(text);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("client disconnected: " + tcpConnection.toString());
    }

    @Override
    public synchronized void onExcepton(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exeption " + e);
    }

    private void sendToAllConnections(String text){
        System.out.println(text);
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).sendString(text);
        }
    }
}
