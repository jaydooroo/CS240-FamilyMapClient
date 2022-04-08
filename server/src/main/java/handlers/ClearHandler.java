package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.ClearRequest;
import result.ClearResult;
import services.ClearService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")){

                //  operating register service


                ClearService clearService  = new ClearService();
                ClearRequest clearRequest =  convertJsonIntoObj(exchange);
                ClearResult clearResult = clearService.clearDB(clearRequest);
                success = clearService.isSuccess();

                if(success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    writeJsonIntoRespond(exchange,clearResult);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    writeJsonIntoRespond(exchange,clearResult);
                }

            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            //Configuring register response body
            ClearResult clearResult = new ClearResult(e.getMessage(), false);

            writeJsonIntoRespond(exchange,clearResult);
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }
    }

    private ClearRequest convertJsonIntoObj(HttpExchange exchange) {

        Reader reqBody  = new InputStreamReader(exchange.getRequestBody());

        // convert Json into Request class format
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ClearRequest clearRequest = gson.fromJson(reqBody,ClearRequest.class);

        return clearRequest;
    }

    private void writeJsonIntoRespond (HttpExchange exchange,ClearResult clearResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // TODO: Write condition statement, possible error: Request property missing or has invalid value,
        //  Username already taken by another user, Internal server error
        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(clearResult);
        OutputStream os = exchange.getResponseBody();
        //TODO: check if this method work
        os.write(respondJson.getBytes());
    }

}
