package com.geekbrains.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static java.nio.file.StandardOpenOption.APPEND;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField;

    @FXML
    HBox msgPanel, authPanel;

    @FXML
    PasswordField passField;

    @FXML
    ListView<String> clientsList;

    private boolean authenticated;
    private String nickname;
    private String login;
    private Path pathToHist;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);
        if (!authenticated) {
            nickname = "";
        } else {
            //если авторизовались, то последний логин, под которым происходила попытка авторизации = текущий
            initTextArea();
        }
    }

    public void initTextArea(){
        // чтение из файла выполняем по логину. а не по нику, т.к. ник может смениться, но юзер тот же
        textArea.clear();
        try {
            pathToHist = Paths.get("history\\" + login + ".txt");
            if(!Files.exists(pathToHist)) Files.createFile(pathToHist);
            List<String> ltStr = Files.readAllLines(pathToHist);
            //для теста сделал выводить последние 14 сообщений
            for (int i = ltStr.size(); i > 0 && i > ltStr.size() - 14; i--)
                textArea.insertText(0,ltStr.get(i-1) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
        clientsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String nickname = clientsList.getSelectionModel().getSelectedItem();
                msgField.setText("/w " + nickname + " ");
                msgField.requestFocus();
                msgField.selectEnd();
            }
        });
        linkCallbacks();
    }

    @FXML
    public void shutdown() {
        //закрытие фйала
    }

    public void sendAuth() {
        Network.sendAuth(loginField.getText(), passField.getText());
        login = loginField.getText(); //последний логин, под которым происходила попытка авторизации
        loginField.clear();
        passField.clear();
    }

    public void sendMsg() {
        if (Network.sendMsg(msgField.getText())) {
            msgField.clear();
            msgField.requestFocus();
        }
    }

    public void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void setClientList(String msg) {
        String[] tokens = msg.split("\\s");
        Platform.runLater(() -> {
            clientsList.getItems().clear();
            for (int i = 1; i < tokens.length; i++) {
                clientsList.getItems().add(tokens[i]);
            }
        });
    }


    public void linkCallbacks() {
        Network.setCallOnException(args -> showAlert(args[0].toString()));

        Network.setCallOnCloseConnection(args -> setAuthenticated(false));

        Network.setCallOnAuthenticated(args -> {
            setAuthenticated(true);
            nickname = args[0].toString();
        });

        Network.setCallOnMsgReceived(args -> {
            String msg = args[0].toString();
            if (msg.startsWith("/")) {
                if (msg.startsWith("/clients "))
                    setClientList(msg);
            } else {
                textArea.appendText(msg + "\n");
                //запись в файл под текущим логином
                try {
                    Files.write(pathToHist, Collections.singleton(msg),APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}