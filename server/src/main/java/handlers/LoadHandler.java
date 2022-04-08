package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.LoadResult;
import services.LoadService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;


        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //  operating register service
                LoadService loadService = new LoadService();
                LoadRequest loadRequest = convertJsonIntoObj(exchange);
                LoadResult loadResult = loadService.load(loadRequest);
                success = loadService.isSuccess();

                if (success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    writeJsonIntoRespond(exchange, loadResult);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    writeJsonIntoRespond(exchange, loadResult);
                }

            }

        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            //Configuring register response body
            LoadResult loadResult = new LoadResult("Error:Internal server error", false);

            writeJsonIntoRespond(exchange, loadResult);
            e.printStackTrace();

        } finally {
            exchange.getResponseBody().close();
        }
    }

    private LoadRequest convertJsonIntoObj(HttpExchange exchange) {
        Reader reqBody = new InputStreamReader(exchange.getRequestBody());

        // convert Json into Request class format
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LoadRequest loadRequest = gson.fromJson(reqBody,LoadRequest.class);

        return loadRequest;
    }

    private void writeJsonIntoRespond(HttpExchange exchange, LoadResult loadResult) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert into Json and put into respond outputStream
        String respondJson = gson.toJson(loadResult);
        OutputStream os = exchange.getResponseBody();
        os.write(respondJson.getBytes());
    }
}
