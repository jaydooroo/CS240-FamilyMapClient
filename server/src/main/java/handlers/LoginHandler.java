package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoginRequest;
import result.LoginResult;
import services.LoginService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoginService loginService = new LoginService();
                LoginRequest loginRequest = convertJsonIntoObj(exchange);
                LoginResult loginResult = loginService.login(loginRequest);

                success = loginService.isSuccess();

                if(success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    writeJsonIntoRespond(exchange,loginResult);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                    writeJsonIntoRespond(exchange,loginResult);


                }

            }
        } catch (Exception e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            LoginResult loginResult = new LoginResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange,loginResult);
            e.printStackTrace();

        }
        finally {
            exchange.getResponseBody().close();
        }


    }


    private LoginRequest convertJsonIntoObj(HttpExchange exchange) {

        Reader reqBody = new InputStreamReader(exchange.getRequestBody());

        // convert Json into Request class format
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LoginRequest loginRequest = gson.fromJson(reqBody,LoginRequest.class);

        return loginRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange, LoginResult loginResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String respondJson = gson.toJson(loginResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }


}
