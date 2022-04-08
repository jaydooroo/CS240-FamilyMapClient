package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import request.FillRequest;
import result.FillResult;
import services.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")){

                //  operating register service
                FillService fillService  = new FillService();
                FillRequest fillRequest =  convertURIIntoObj(exchange);
                FillResult fillResult = fillService.fill(fillRequest);
                success = fillService.isSuccess();

                if(success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    writeJsonIntoRespond(exchange,fillResult);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    writeJsonIntoRespond(exchange,fillResult);

                }

            }

        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            //Configuring register response body
            FillResult fillResult = new FillResult("Error:Error:Internal server error",false);
            writeJsonIntoRespond(exchange,fillResult);
            e.printStackTrace();

        } catch (DataAccessException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            FillResult fillResult = new FillResult(e.getMessage(),false);
            writeJsonIntoRespond(exchange,fillResult);
            e.printStackTrace();

        } finally {
            exchange.getResponseBody().close();
        }
    }

    private FillRequest convertURIIntoObj(HttpExchange exchange) throws DataAccessException{


        // convert Json into Request class format
        String theURI = exchange.getRequestURI().toString();

        String[] splitURI = theURI.split("/");

        if(splitURI.length < 3) {
            throw new DataAccessException("Username is required. should be ex) /fill/Ann or /fill/Ann/3");
        }
        else if(splitURI.length > 4) {
            throw new DataAccessException("Too many fill instructions. should be ex) /fill/Ann or /fill/Ann/3");
        }
        String username = splitURI[2];
        int generations = 4;
        if(splitURI.length == 4) {
            generations = Integer.parseInt(splitURI[3]);
        }

        FillRequest fillRequest = new FillRequest(username,generations);

        return fillRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange, FillResult fillResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String respondJson = gson.toJson(fillResult);
        OutputStream os = exchange.getResponseBody();
        os.write(respondJson.getBytes());
    }



}
