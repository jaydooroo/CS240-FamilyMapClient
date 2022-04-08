package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import request.SinglePersonRequest;
import result.SinglePersonResult;
import services.SinglePersonService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class SinglePersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if(reqHeaders.containsKey("Authorization")) {

                    String authtoken = reqHeaders.getFirst("Authorization");

                    SinglePersonService singlePersonService = new SinglePersonService();
                    SinglePersonRequest singlePersonRequest = convertURIIntoObj(exchange);
                    singlePersonRequest.setAuthtoken(authtoken);

                    SinglePersonResult singlePersonResult = singlePersonService.singlePerson(singlePersonRequest);

                    success = singlePersonService.isSuccess();

                    if(success) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                        writeJsonIntoRespond(exchange, singlePersonResult);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        writeJsonIntoRespond(exchange,singlePersonResult);
                    }
                }
                else {
                    throw new DataAccessException("Error:Does not have a authtoken");
                }

            }
        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            SinglePersonResult singlePersonResult = new SinglePersonResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange, singlePersonResult);
            e.printStackTrace();

        } catch (DataAccessException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);

            SinglePersonResult singlePersonResult = new SinglePersonResult(e.getMessage(), false);

            writeJsonIntoRespond(exchange, singlePersonResult);

            e.printStackTrace();

        }
        finally {
            exchange.getResponseBody().close();
        }


    }


    private SinglePersonRequest convertURIIntoObj(HttpExchange exchange) throws DataAccessException {

        InputStream reqBody  = exchange.getRequestBody();

        String theURI = exchange.getRequestURI().toString();

        String[] splitURI = theURI.split("/");

        //TODO: throw 관리하기
        if(splitURI.length != 3) {
            throw new DataAccessException("Does not have a Person ID or too many parameters should be " +
                    "ex) /person/7255e93e");
        }
        String personID = splitURI[2];

        SinglePersonRequest singlePersonRequest = new SinglePersonRequest(personID);

        return singlePersonRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange, SinglePersonResult singlePersonResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // TODO: Write condition statement, possible error: Request property missing or has invalid value,
        //  Username already taken by another user, Internal server error
        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(singlePersonResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes(StandardCharsets.UTF_8));
    }

}
