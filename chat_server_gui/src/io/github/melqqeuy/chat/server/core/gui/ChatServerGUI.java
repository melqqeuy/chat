package io.github.melqqeuy.chat.server.core.gui;

import io.github.melqqeuy.chat.server.core.ChatServer;
import io.github.melqqeuy.chat.server.core.ChatServerListener;
import io.github.melqqeuy.chat.server.library.DefaultGUIExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatServerGUI extends JFrame implements ActionListener, ChatServerListener {

    private static final int POS_X = 1100;
    private static final int POS_Y = 150;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

    private static final String TITLE = "Chat Server";
    private static final String START_LISTENING = "Start Listening";
    private static final String DROP_ALL_CLIENTS = "Drop All Clients";
    private static final String STOP_LISTENING = "Stop Listening";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatServerGUI();
            }
        });
    }

    private final ChatServer chatServer = new ChatServer(this);
    private final JButton btnStartListening = new JButton(START_LISTENING);
    private final JButton btnStopListening = new JButton(STOP_LISTENING);
    private final JButton btnDropAllClients = new JButton(DROP_ALL_CLIENTS);
    private final JTextArea log = new JTextArea();

    private ChatServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultGUIExceptionHandler());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setTitle(TITLE);

        btnStartListening.addActionListener(this);
        btnDropAllClients.addActionListener(this);
        btnStopListening.addActionListener(this);

        JPanel upperPanel = new JPanel(new GridLayout(1, 3));
        upperPanel.add(btnStartListening);
        upperPanel.add(btnDropAllClients);
        upperPanel.add(btnStopListening);
        add(upperPanel, BorderLayout.NORTH);

        JScrollPane scrollLog = new JScrollPane(log);
        log.setEditable(false);
        add(scrollLog, BorderLayout.CENTER);

        //setAlwaysOnTop(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStartListening) {
            chatServer.startListening(8189);
        } else if (src == btnDropAllClients) {
            chatServer.dropAllClients();
        } else if (src == btnStopListening) {
            chatServer.stopListening();
        } else {
            throw new RuntimeException("Unknown src = " + src);
        }
    }

    @Override
    public void onLogChatServer(ChatServer chatServer, String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
