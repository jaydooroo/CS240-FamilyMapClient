package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {




    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        //get request url method?
        //exchange
        //request method is get
        // get url using url look for a file in the system.
        //exchange respond body return the according file.


        try {

            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                //TODO: check if it should all ok even when we send 404.html file
                // checx when I should sen the 404.html
                String urlPath = exchange.getRequestURI().toString();

                //why dose it not work with ==
                if (urlPath.equals("/")) {
                    // 왜 작동이 안되나?
                    urlPath = "web/index.html";
                }else{
                    urlPath = "web" + urlPath;
                }

                File resultFile = new File(urlPath);

                if(resultFile.isFile()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(resultFile.toPath(), respBody);
                }
                else {
                    // 여기서 어떤 request 넣어야 하나?
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    urlPath = "web/HTML/404.html";
                    resultFile = new File(urlPath);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(resultFile.toPath(), respBody);
                }
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        finally {
            exchange.getResponseBody().close();
        }


    }
}
