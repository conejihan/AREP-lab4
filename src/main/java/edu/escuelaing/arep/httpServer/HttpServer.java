package edu.escuelaing.arep.httpServer;
import edu.escuelaing.arep.spring.components.Request;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import static edu.escuelaing.arep.spring.SpringApplication.invoke;

public class HttpServer {

    private static final String ROUTE_TO_STATIC_FILES = "/src/main/resources/static";
    private static final String BASIC_HTML_ERROR_START = "<!DOCTYPE html>\n <html>\n<head>\n<meta charset=\"UTF-8\">\n<title>";
    public static final String BASIC_HTML_ERROR_MEDIUM = " Error</title>\n</head>\n<body>\n";
    public static final String BASIC_HTML_END = "</body>\n</html>\n";
    private final Map<String, Method> componentsRoute;
    private ServerSocket serverSocket;
    private OutputStream out;
    private BufferedReader in;
    private boolean running;

    public HttpServer(Map<String, Method> componentsRoute) {
        super();
        this.componentsRoute = componentsRoute;
    }


    public void startServer() throws IOException, InvocationTargetException, IllegalAccessException {
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(1);
        }
        startAcceptingRequests();
    }


    public void getStaticFiles(String endpoint) {
        String fullPath = ROUTE_TO_STATIC_FILES + endpoint;
        if (endpoint.contains("jpg")) {
            getImage(fullPath);
        } else if (endpoint.contains("html") || endpoint.contains("js")) {
            getResources(fullPath);
        }
    }


    public void getResources(String fullPath) {
        String type = fullPath.split("\\.")[1];
        if (type.equals("js")) type = "json";
        try {
            in = new BufferedReader(new FileReader(System.getProperty("user.dir") + fullPath));
            String outLine = "";
            String line;
            while ((line = in.readLine()) != null) {
                outLine += line;
            }
            out.write(("HTTP/1.1 201 OK\r\n"
                    + "Content-Type: text/" + type + ";"
                    + "charset=\"UTF-8\" \r\n"
                    + "\r\n"
                    + outLine).getBytes());
        } catch (IOException e) {
            int statusCode = 404;
            errorMessage(statusCode,
                    BASIC_HTML_ERROR_START + statusCode + BASIC_HTML_ERROR_MEDIUM
                            + "<h1>404 File Not Found</h1>\n"
                            + BASIC_HTML_END, "Not Found");
        }
    }


    public void getImage(String fullPath) {
        String type = fullPath.split("\\.")[1];
        try {
            BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + fullPath));
            ByteArrayOutputStream arrBytes = new ByteArrayOutputStream();
            DataOutputStream outImage = new DataOutputStream(out);
            ImageIO.write(image, type, arrBytes);
            outImage.writeBytes("HTTP/1.1 200 OK \r\n"
                    + "Content-Type: image/" + type + " \r\n"
                    + "\r\n");
            out.write(arrBytes.toByteArray());
        } catch (IOException e) {
            int statusCode = 404;
            errorMessage(statusCode,
                    BASIC_HTML_ERROR_START + statusCode + BASIC_HTML_ERROR_MEDIUM
                            + "<h1>404 File Not Found</h1>\n"
                            + BASIC_HTML_END, "Not Found");
        }
    }


    public void errorMessage(int statusCode, String message, String statusName) {
        try {
            out.write(("HTTP/1.1 " + statusCode + " " + statusName + "\r\n"
                    + "\r\n"
                    + message).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public OutputStream getOut() {
        return out;
    }


    public void setOut(OutputStream out) {
        this.out = out;
    }

    private void startAcceptingRequests() throws IOException, InvocationTargetException, IllegalAccessException {
        running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            receiveRequest(clientSocket);
        }
        serverSocket.close();
    }

    private void receiveRequest(Socket clientSocket) throws IOException, InvocationTargetException, IllegalAccessException {
        out = clientSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        String endpoint;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("GET")) {
                endpoint = inputLine.split(" ")[1];
                if (endpoint.equals("/")) {
                    getStaticFiles("/index.html");
                } else if (endpoint.contains("/Nsapps")) {
                    endpoint = endpoint.replace("/Nsapps", "");
                    executeSpringEndpoint(endpoint, out);
                } else {
                    getStaticFiles(endpoint);
                }
            }
            if (!in.ready()) break;
        }
        in.close();
        clientSocket.close();
    }

    private void executeSpringEndpoint(String path, OutputStream out) throws IOException, InvocationTargetException, IllegalAccessException {
        Request request = new Request(path);
        String endpoint = request.getEndpoint();
        String value = request.getValue();
        if (componentsRoute.containsKey(endpoint)) {
            String result;

            if (value == null) {
                result = invoke(componentsRoute.get(endpoint));
            } else {
                result = invoke(componentsRoute.get(endpoint), value);
            }
            out.write(("HTTP/1.1 200 OK\r\n"
                    + "\r\n"
                    + result).getBytes());

        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4444;
    }
}
