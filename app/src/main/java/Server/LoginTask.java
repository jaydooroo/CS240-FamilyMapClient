package Server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import Data.DataCache;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private String serverPort;
    private String serverHost;
    private String username;
    private String password;

    public LoginTask(Handler messageHandler, String serverHost, String serverPort, String username,
                     String password) {
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {

        ServerProxy serverProxy = new ServerProxy(serverHost,serverPort);

        LoginRequest loginRequest = new LoginRequest(username,password);
        LoginResult loginResult;
        try {
            loginResult =  serverProxy.login(loginRequest);
            if(!loginResult.isSuccess()){
                throw new IOException("Login Failed");
            }

            DataCache dataCache = DataCache.getInstance();

            dataCache.setAuthtoken(loginResult.getAuthtoken());
            dataCache.setUsername(loginResult.getUsername());
            dataCache.setPersonID(loginResult.getPersonID());


            sendMessage(true);

        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(false);

        }
    }

    private void sendMessage(Boolean success) {

        Message message = Message.obtain();
        Bundle messageBundle= new Bundle();
        messageBundle.putBoolean("SUCCESS_KEY",success);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }

}
