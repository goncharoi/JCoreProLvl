package com.geekbrains.server;

public interface DBService { //Обобщил и переименовал интерфейс
    String getNicknameByLoginAndPassword(String login, String password);

    boolean changeNicknameByLoginAndPassword(String nick, String login, String password);
}
