package org.example;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpServer {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        boolean running = true;
        
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String requestUri = "";
            byte[] header, body;
            boolean firstLine = true;
             
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    requestUri = inputLine.split(" ")[1];
                    //System.out.println("URI: " + requestUri);
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI request = new URI(requestUri);
            if (request.getPath().equals("/cliente")) {
                request = new URI("/index.html");
            }
            System.out.println(request.getPath());
            try {
                header = HttpResponseHeader(request);
                body = HttpResponseBody(request);
                out.write(header);
                out.write(body);
            } catch (Exception e) {
                header = HttpResponseHeaderError();
                body = HttpResponseBodyError();
                out.write(header);
                out.write(body);
            }
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static byte[] HttpResponseHeader(URI requestURI) {
        String type = getContesttype(requestURI.getPath());
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type:" + type + "\r\n"
                + "\r\n";
        return outputLine.getBytes();
    }

    private static byte[] HttpResponseBody(URI requestURI) throws IOException  {
            return Files.readAllBytes(Paths.get("target/classes/resources" + requestURI.getPath()));
    }

    private static byte[] HttpResponseHeaderError() {
        String outputLine = "HTTP/1.1 400 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
        return outputLine.getBytes();
    }

    private static byte[] HttpResponseBodyError() {
        String outputLine = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Not Found</h1>\n"
                + "</body>\n"
                + "</html>\n";
        return outputLine.getBytes();
    }

    private static String getContesttype(String uri) {

        if (uri.endsWith(".html")) {
            return "text/html";
        } else if (uri.endsWith(".css")) {
            return "text/css";
        } else if (uri.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "text/htlm";
        }
    }
}