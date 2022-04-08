package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import request.AllEventRequest;
import result.AllEventResult;
import services.AllEventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class AllEventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if(reqHeaders.containsKey("Authorization")) {
                    String authtoken = reqHeaders.getFirst("Authorization");

                    AllEventService allEventService = new AllEventService();
                    AllEventRequest allEventRequest = new AllEventRequest();
                    allEventRequest.setAuthtoken(authtoken);

                    AllEventResult allEventResult = allEventService.event(allEventRequest);

                    success = allEventService.isSuccess();

                    if (success) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        writeJsonIntoRespond(exchange, allEventResult);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        writeJsonIntoRespond(exchange, allEventResult);
                    }
                }
                else {

                    throw new DataAccessException("Error:Does not have a authtoken");
                }

            }
        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            AllEventResult allEventResult = new AllEventResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange,allEventResult);
            e.printStackTrace();

        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            AllEventResult allEventResult = new AllEventResult(e.getMessage(), false);
            writeJsonIntoRespond(exchange, allEventResult);
            e.printStackTrace();
        } finally {
            exchange.getResponseBody().close();
        }


    }



    private void writeJsonIntoRespond (HttpExchange exchange, AllEventResult allEventResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // TODO: Write condition statement, possible error: Request property missing or has invalid value,
        //  Username already taken by another user, Internal server error
        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(allEventResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }



}
