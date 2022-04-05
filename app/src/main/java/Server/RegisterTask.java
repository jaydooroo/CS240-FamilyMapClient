package Server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import Data.DataCache;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask implements Runnable {

    private final Handler messageHandler;
    private String serverPort;
    private String serverHost;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    public RegisterTask(Handler messageHandler, String serverHost, String serverPort, String username, String password, String email, String firstName, String lastName, String gender) {
        this.messageHandler = messageHandler;
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }


    @Override
    public void run() {

        ServerProxy serverProxy = new ServerProxy(serverHost,serverPort);

        RegisterRequest registerRequest =new RegisterRequest(username,password,email,firstName,lastName,gender);
        RegisterResult registerResult;

        try {
            registerResult = serverProxy.register(registerRequest);

            if(!registerResult.isSuccess()) {
                throw new IOException("Register Failed");
            }

            DataCache dataCache = DataCache.getInstance();

            dataCache.setAuthtoken(registerResult.getAuthtoken());
            dataCache.setUsername(registerResult.getUsername());
            dataCache.setPersonID(registerResult.getPersonID());

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
