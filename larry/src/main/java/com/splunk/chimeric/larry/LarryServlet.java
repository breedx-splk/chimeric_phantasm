package com.splunk.chimeric.larry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LarryServlet extends HttpServlet {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final URI CURLY_NYUK_URI = URI.create("http://localhost:8080/curly/nyuk");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        if ("/nyuk".equals(requestPath)) {
            handleNyuk(response);
            return;
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("larry.txt")) {
            if (input == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing resource: larry.txt");
                return;
            }

            response.getWriter().write(new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private void handleNyuk(HttpServletResponse response) throws IOException {
        HttpRequest curlyRequest = HttpRequest.newBuilder(CURLY_NYUK_URI).GET().build();

        try {
            HttpResponse<String> curlyResponse = HTTP_CLIENT.send(
                    curlyRequest,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
            response.getWriter().write(curlyResponse.body() + " nyuk");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Interrupted calling curly /nyuk");
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failed calling curly /nyuk");
        }
    }
}
