package io.github.melqqeuy.network;

import java.net.Socket;

public interface SocketThreadListener {

    void onStartSockedThread(SocketThread socketThread);
    void onStopSockedThread(SocketThread socketThread);
    void onReadySockedThread(SocketThread socketThread, Socket socket);
    void onReceiveStringSocketThread(SocketThread socketThread, Socket socket, String value);
    void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e);
}
