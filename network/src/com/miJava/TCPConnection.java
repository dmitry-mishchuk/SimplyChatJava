package com.miJava;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by on 29.03.2021.
 */
public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnoctionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnoctionListener eventListener, String ipAdress, int port) throws IOException{
        this(eventListener, new Socket(ipAdress, port));
    }

    public TCPConnection(TCPConnoctionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream(), Charset.forName("UTF-8")));
            rxThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        eventListener.onConnectionReady(TCPConnection.this);
                        while (!rxThread.isInterrupted()){
                            String msg = in.readLine();
                            eventListener.onReceiveString(TCPConnection.this, msg);
                        }
                    } catch (IOException e) {
                        eventListener.onExcepton(TCPConnection.this, e);
                    } finally {
                        eventListener.onDisconnect(TCPConnection.this);
                    }
                }
            });
        rxThread.start();
    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onExcepton(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onExcepton(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection " + socket.getInetAddress() +
                " : " + socket.getPort();
    }
}
