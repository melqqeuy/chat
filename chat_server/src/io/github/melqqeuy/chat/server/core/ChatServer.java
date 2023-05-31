package io.github.melqqeuy.chat.server.core;

import io.github.melqqeuy.network.ServerSocketThread;
import io.github.melqqeuy.network.ServerSocketThreadListener;
import io.github.melqqeuy.network.SocketThread;
import io.github.melqqeuy.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private final ChatServerListener eventListener;
    private ServerSocketThread serverSocketThread;
    private final Vector<SocketThread> clients = new Vector<>();
    public ChatServer(ChatServerListener eventListener) {
        this.eventListener = eventListener;
    }

    public void startListening(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            putLog("Поток сервера уже запущен.");
            return;
        }
        serverSocketThread = new ServerSocketThread(this,"ServerSocketThread", port, 2000);
    }

    public void dropAllClients() {
        putLog("dropAllClients");
    }

    public void stopListening() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            putLog("Поток сервера не запущен.");
            return;
        }
        serverSocketThread.interrupt();
    }

    private synchronized void putLog(String msg) {
        eventListener.onLogChatServer(this, msg);
    }

    @Override
    public void onStartServerSocketThread(ServerSocketThread thread) {
        putLog("start...");
    }

    @Override
    public void onStopServerSocketThread(ServerSocketThread thread) {
        putLog("stopped.");
    }

    @Override
    public void onReadyServerSocketThread(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("ServerSocket is ready...");
    }

    @Override
    public void onTimeOutAccept(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("accept() timeout");
    }

    @Override
    public void onAcceptedSocket(ServerSocketThread thread, ServerSocket serverSocket, Socket socket) {
        putLog("Client connected: " + socket);
        String threadName = "Socket thread: " + socket.getInetAddress() + ":" + socket.getPort();
        new SocketThread(this, threadName, socket);
    }

    @Override
    public void onExceptionServerSocketThread(ServerSocketThread thread, Exception e) {
        putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());
    }

    @Override
    public synchronized void onStartSockedThread(SocketThread socketThread) {
        putLog("start...");
    }

    @Override
    public synchronized void onStopSockedThread(SocketThread socketThread) {
        putLog("stopped.");
        clients.remove(socketThread);
    }

    @Override
    public synchronized void onReadySockedThread(SocketThread socketThread, Socket socket) {
        putLog("Socket is ready...");
        clients.add(socketThread);
    }

    @Override
    public synchronized void onReceiveStringSocketThread(SocketThread socketThread, Socket socket, String value) {
        socketThread.sendMsg(value);
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendMsg(value);
        }
    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread socketThread, Socket socket, Exception e) {
        putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());
    }
}
