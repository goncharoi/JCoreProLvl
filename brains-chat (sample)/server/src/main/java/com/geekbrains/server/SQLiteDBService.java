package com.geekbrains.server;

import java.sql.*;

public class SQLiteDBService implements DBService {

    private static SQLiteDBService instance;

    private Connection connection;

    public static SQLiteDBService getInstance(){
        if (instance == null) instance = new SQLiteDBService();
        return instance;
    }

    public static void freeInstance(){
        if (instance != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

    private SQLiteDBService() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:ChatDB.db");
            System.out.println("Создано соединение с БД");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String lvReturn = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT nick FROM users WHERE login = ? AND password = ?"
            );
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) lvReturn = resultSet.getString("nick");
            System.out.println("Запрос ника вернул:"+lvReturn);
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lvReturn;
    }

    @Override
    public boolean changeNicknameByLoginAndPassword(String nick, String login, String password) {
        //При смене ника повторно запрашиваются логин и пароль, чтобы не хранить их на сервере - не знаю, правильно ли это
        boolean lvReturn = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE users SET nick = ? WHERE login = ? AND password = ?"
            );
            preparedStatement.setString(1, nick);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            if (preparedStatement.executeUpdate() == 1) lvReturn = true;
            System.out.println("Ник сменен на "+nick);
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lvReturn;
    }
}
