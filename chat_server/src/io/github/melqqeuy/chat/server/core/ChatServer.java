package io.github.melqqeuy.chat.server.core;

public class ChatServer {
    private final ChatServerListener eventListener;
    public ChatServer(ChatServerListener eventListener) {
        this.eventListener = eventListener;
    }

    public void startListening(int port) {
        putLog("Сервер запущен.");
    }

    public void dropAllClients() {
        putLog("dropAllClients");
    }

    public void stopListening() {
        putLog("Сервер остановлен.");
    }

    private void putLog(String msg) {
        eventListener.onLogChatServer(this, msg);
    }
}
