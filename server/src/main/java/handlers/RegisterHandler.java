package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.RegisterResult;
import services.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")){

                //  operating register service

                RegisterService registerService  = new RegisterService();
                RegisterRequest registerRequest =  convertJsonIntoObj(exchange);
                RegisterResult registerResult = registerService.register(registerRequest);
                success = registerService.isSuccess();

                if(success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    writeJsonIntoRespond(exchange,registerResult);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    writeJsonIntoRespond(exchange,registerResult);
                }

            }

        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            //Configuring register response body
            RegisterResult registerResult = new RegisterResult("Error:Internal server error",false);

            writeJsonIntoRespond(exchange,registerResult);
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }
    }

    private RegisterRequest convertJsonIntoObj(HttpExchange exchange) {

        Reader reqBody = new InputStreamReader(exchange.getRequestBody());

        // convert Json into Request class format
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RegisterRequest registerRequest =(RegisterRequest) gson.fromJson(reqBody,RegisterRequest.class);

        return registerRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange,RegisterResult registerResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String respondJson = gson.toJson(registerResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }

}
