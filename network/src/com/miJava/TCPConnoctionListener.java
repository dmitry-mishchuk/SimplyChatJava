package com.miJava;

/**
 * Created by on 29.03.2021.
 */
public interface TCPConnoctionListener {
    void onConnectionReady(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String text);
    void onDisconnect(TCPConnection tcpConnection);
    void onExcepton (TCPConnection tcpConnection, Exception e);

}
