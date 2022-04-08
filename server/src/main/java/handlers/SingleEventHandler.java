package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import request.SingleEventRequest;
import result.SingleEventResult;
import services.SingleEventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class SingleEventHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    String authtoken = reqHeaders.getFirst("Authorization");

                    SingleEventService singleEventService = new SingleEventService();
                    SingleEventRequest singleEventRequest = convertURIIntoObj(exchange);
                    singleEventRequest.setAuthtoken(authtoken);

                    SingleEventResult singleEventResult = singleEventService.singleEvent(singleEventRequest);

                    success = singleEventService.isSuccess();

                    if (success) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        writeJsonIntoRespond(exchange, singleEventResult);

                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                        writeJsonIntoRespond(exchange,singleEventResult);
                    }
                }
                else {
                    throw new DataAccessException("Error:Does not have a authtoken");
                }
            }
        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            SingleEventResult singleEventResult = new SingleEventResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange,singleEventResult);
            e.printStackTrace();

        } catch (DataAccessException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            SingleEventResult singleEventResult = new SingleEventResult(e.getMessage(), false);

            writeJsonIntoRespond(exchange,singleEventResult);

            e.printStackTrace();

        }
        finally {
            exchange.getResponseBody().close();
        }


    }


    private SingleEventRequest convertURIIntoObj(HttpExchange exchange) throws DataAccessException {

        String theURI = exchange.getRequestURI().toString();

        String[] splitURI = theURI.split("/");

        //TODO: throw 관리하기
        if(splitURI.length != 3) {
            throw new DataAccessException("Does not have a Event ID or too many parameters should be " +
                    "ex) /event/7255e93e");
        }
        String eventID = splitURI[2];

        SingleEventRequest singleEventRequest = new SingleEventRequest(eventID);

        return singleEventRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange, SingleEventResult singleEventResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // TODO: Write condition statement, possible error: Request property missing or has invalid value,
        //  Username already taken by another user, Internal server error
        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(singleEventResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }

}
