package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import request.AllEventRequest;
import request.AllPersonRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.AllPersonResult;
import result.LoginResult;
import result.RegisterResult;

public class ServerProxy {
    private String serverHost;
    private String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest loginRequest) throws IOException {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.connect();

            convertIntoJson(http,loginRequest);
            LoginResult loginResult = null;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Reader repBody = new InputStreamReader(http.getInputStream());

            // convert Json into Result class format
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            loginResult = gson.fromJson(repBody, LoginResult.class);
        }
        else {
            throw new IOException("Http Access Error");
        }
           return  loginResult;
    }


    public RegisterResult register(RegisterRequest registerRequest) throws IOException {

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

        HttpURLConnection http = (HttpURLConnection)url.openConnection();

        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");

        convertIntoJson(http,registerRequest);


        RegisterResult registerResult = null;
       if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
           Reader repBody = new InputStreamReader(http.getInputStream());

           // convert Json into Result class format
           Gson gson = new GsonBuilder().setPrettyPrinting().create();
           registerResult = gson.fromJson(repBody, RegisterResult.class);
           return registerResult;

       }else {
           throw new IOException("Http Access Error");
       }

    }

    public AllPersonResult getAllPeople(AllPersonRequest allPersonRequest) throws IOException {

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(false);
        http.addRequestProperty("Authorization", allPersonRequest.getAuthtoken());
        http.addRequestProperty("Accept", "application/json");

        AllPersonResult allPersonResult = null;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Reader repBody = new InputStreamReader(http.getInputStream());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            allPersonResult = gson.fromJson(repBody, AllPersonResult.class);
        }
        else {

            throw new IOException("Http Access Error");
        }

        return  allPersonResult;
    }

    public AllEventResult getAllEvents(AllEventRequest allEventRequest) throws IOException{
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(false);
        http.addRequestProperty("Authorization", allEventRequest.getAuthtoken());

        AllEventResult allEventResult;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {

            Reader repBody = new InputStreamReader(http.getInputStream());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            allEventResult = gson.fromJson(repBody, AllEventResult.class);
        }
        else {

            throw new IOException("Http Access Error");
        }

        return  allEventResult;
    }

    private void convertIntoJson (HttpURLConnection http, Object obj) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String respondJson = gson.toJson(obj);
        OutputStream os = http.getOutputStream();
        //TODO: check if this method work
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(respondJson);
        sw.flush();
        os.close();
    }

}
