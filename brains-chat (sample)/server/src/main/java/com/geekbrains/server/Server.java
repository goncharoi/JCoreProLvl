package com.geekbrains.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {
    private Vector<ClientHandler> clients;
    private DBService authService;

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName()); //ДЗ 6: создаем логгер для данного класса

    public DBService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = SQLiteDBService.getInstance(); //new SimpleAuthService();

        //ДЗ 4: сервис создан в классе сервера, чтобы обеспечить закрытие потоков
        // newCachedThreadPool - потому что число потоков заранее неизвестно
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            LOGGER.info("Сервер запущен на порту 8189"); //ДЗ 6: выводим сообщение в лог
            while (true) {
                Socket socket = serverSocket.accept();

                new ClientHandler(this, socket, executorService); // ДЗ 4: передаю executorService туда, где создаются потоки

                LOGGER.info("Подключился новый клиент"); //ДЗ 6: выводим сообщение в лог
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            SQLiteDBService.freeInstance();

            executorService.shutdown(); //ДЗ 4: закрытие потоков
        }
        LOGGER.info("Сервер завершил свою работу"); //ДЗ 6: выводим сообщение в лог
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public void privateMsg(ClientHandler sender, String receiverNick, String msg) {
        if (sender.getNickname().equals(receiverNick)) {
            sender.sendMsg("заметка для себя: " + msg);
            return;
        }
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(receiverNick)) {
                o.sendMsg("от " + sender.getNickname() + ": " + msg);
                sender.sendMsg("для " + receiverNick + ": " + msg);
                return;
            }
        }
        sender.sendMsg("Клиент " + receiverNick + " не найден");
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        // '/clients '
        for (ClientHandler o : clients) {
            sb.append(o.getNickname()).append(" ");
        }
        // '/clients nick1 nick2 nick3 '
        sb.setLength(sb.length() - 1);
        // '/clients nick1 nick2 nick3'
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }
}
