package com.splunk.chimeric.moe;

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

public class MoeServlet extends HttpServlet {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final URI LARRY_NYUK_URI = URI.create("http://localhost:8080/larry/nyuk");
    private static final URI LARRY_SING_URI = URI.create("http://localhost:8080/larry/sing");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        if ("/nyuk".equals(requestPath)) {
            handleNyuk(response);
            return;
        }
        if ("/sing".equals(requestPath)) {
            handleSing(response);
            return;
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("moe.txt")) {
            if (input == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing resource: moe.txt");
                return;
            }

            response.getWriter().write(new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private void handleNyuk(HttpServletResponse response) throws IOException {
        HttpRequest larryRequest = HttpRequest.newBuilder(LARRY_NYUK_URI).GET().build();

        try {
            HttpResponse<String> larryResponse = HTTP_CLIENT.send(
                    larryRequest,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
            response.getWriter().write(larryResponse.body() + " nyuk");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Interrupted calling larry /nyuk");
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failed calling larry /nyuk");
        }
    }

    private void handleSing(HttpServletResponse response) throws IOException {
        HttpRequest larryRequest = HttpRequest.newBuilder(LARRY_SING_URI).GET().build();

        try {
            HttpResponse<String> larryResponse = HTTP_CLIENT.send(
                    larryRequest,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
            response.getWriter().write("three " + larryResponse.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Interrupted calling larry /sing");
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failed calling larry /sing");
        }
    }
}
