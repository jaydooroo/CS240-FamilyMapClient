import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;




public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;


    private HttpServer server;

    private void run (String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),MAX_WAITING_CONNECTIONS);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");


        server.createContext("/user/login",new LoginHandler());
        server.createContext("/event/", new SingleEventHandler());
        server.createContext("/event", new AllEventHandler());
        server.createContext("/person/", new SinglePersonHandler());
        server.createContext("/person", new AllPersonHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/", new FileHandler());


        System.out.println("Starting server");
        server.start();

        System.out.println("server started");

    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
