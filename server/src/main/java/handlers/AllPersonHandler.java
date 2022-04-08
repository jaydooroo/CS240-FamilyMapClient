package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import request.AllPersonRequest;
import result.AllPersonResult;
import services.AllPersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class AllPersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if(reqHeaders.containsKey("Authorization")) {
                    String authtoken = reqHeaders.getFirst("Authorization");
                    AllPersonService allPersonService = new AllPersonService();
                    AllPersonRequest allPersonRequest = new AllPersonRequest(authtoken);
                    AllPersonResult allPersonResult = allPersonService.person(allPersonRequest);

                    success = allPersonService.isSuccess();

                    if (success) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        writeJsonIntoRespond(exchange, allPersonResult);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        writeJsonIntoRespond(exchange, allPersonResult);
                    }
                } else {

                    throw new DataAccessException("Error:Does not have a authtoken");
                }
            }
        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            AllPersonResult allPersonResult = new AllPersonResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange,allPersonResult);
            e.printStackTrace();

        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            AllPersonResult allPersonResult = new AllPersonResult(e.getMessage(), false);
            writeJsonIntoRespond(exchange, allPersonResult);
            e.printStackTrace();
        } finally {
            exchange.getResponseBody().close();
        }


    }



    private void writeJsonIntoRespond (HttpExchange exchange, AllPersonResult allPersonResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // TODO: Write condition statement, possible error: Request property missing or has invalid value,
        //  Username already taken by another user, Internal server error
        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(allPersonResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }

}
