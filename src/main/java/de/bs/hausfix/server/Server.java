package de.bs.hausfix.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
//import org.glassfish.jersey.server.spi.ContainerResponseFilter;
//import org.glassfish.jersey.server.spi.ContainerRequestFilter;

import jakarta.ws.rs.core.Application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

//public class Server {
//
//    private static HttpServer server;
//
//    public static void main(String[] args) throws IOException {
//        startServer();
//    }
//
//    public static void startServer() throws IOException {
//        ResourceConfig config = new ResourceConfig(RestApi.class);
//        server = HttpServer.create(new InetSocketAddress(8080), 0);
//
//        // Hier wird der Jersey-Container erstellt
//        server.createContext("/test", new HttpHandler() {
//            @Override
//            public void handle(HttpExchange exchange) throws IOException {
//                // Hier kannst du die Anfragen verarbeiten
//                // Beispiel: Sende eine einfache Antwort zurück
//                String response = "Hello, World!";
//                exchange.sendResponseHeaders(200, response.length());
//                exchange.getResponseBody().write(response.getBytes());
//                exchange.close();
//            }
//        });
//
//        server.setExecutor(null); // creates a default executor
//        server.start();
//        System.out.println("Server gestartet auf http://localhost:8080/test/resources");
//    }
//
//    public static void stopServer() {
//        if (server != null) {
//            server.stop(0);
//            System.out.println("Server gestoppt.");
//        }
//    }
//}
public class Server {
    private static final String PACKAGE_NAME = "de.bs.hausfix.server";
    private static HttpServer server;

    public static void startServer(String url){
        System.out.println("Start server: " + url);
        final ResourceConfig rc = new ResourceConfig()
                .packages(PACKAGE_NAME)
                .register(RestApi.class);
        server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
        System.out.println("Ready for Requests....");
    }

    public static void stopServer(){
        server.stop(0);
    }
}
