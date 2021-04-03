package com.geekbrains.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientHandler {


    private String nickname;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public String getNickname() {
        return nickname;
    }

    public ClientHandler(Server server, Socket socket, ExecutorService executorService) { //ДЗ 4: принимаем сервис
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            executorService.execute(() -> { //ДЗ 4: вместо треда создаем потоки через сервис
                try {
                    getAuth();
                    receiveMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClientHandler.this.disconnect();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg() throws IOException{
        while (true) {
            String msg = in.readUTF();
            if(msg.startsWith("/")) {
                if (msg.equals("/end")) {
                    sendMsg("/end");
                    break;
                }
                if(msg.startsWith("/w ")) {
                    String[] tokens = msg.split("\\s", 3);
                    server.privateMsg(this, tokens[1], tokens[2]);
                }
//>>> Кусок про смену ника
                if(msg.startsWith("/chn")){
                    String[] tokens = msg.split("\\s", 4); //nick login pass
                    if(tokens[1] != null && !server.isNickBusy(tokens[1])){
                        if(server.getAuthService().changeNicknameByLoginAndPassword(tokens[1], tokens[2], tokens[3])) {
                            System.out.println("отправляю клиенту сообщение о смене ника");
                            sendMsg("/authok " + tokens[1]);
                            nickname = tokens[1];
                            System.out.println("Рассылаю новый список ников");
                            server.broadcastClientsList();
                        }
                    }
                }
//<<< Кусок про смену ника
            } else {
                server.broadcastMsg(nickname + ": " + msg);
            }
        }
    }

    public void getAuth() throws IOException{
        while (true) {
            String msg = in.readUTF();
            // /auth login1 pass1
            if (msg.startsWith("/auth ")) {
                System.out.println("Попытка авторизации: "+msg);
                String[] tokens = msg.split("\\s");
                String nick = server.getAuthService().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
                if (nick != null && !server.isNickBusy(nick)) {
                    sendMsg("/authok " + nick);
                    nickname = nick;
                    server.subscribe(this);
                    break;
                }
            }
        }        
    }
    
    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        server.unsubscribe(this);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
