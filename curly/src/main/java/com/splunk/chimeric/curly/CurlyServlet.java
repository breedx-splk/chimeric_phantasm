package com.splunk.chimeric.curly;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CurlyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        if ("/nyuk".equals(requestPath)) {
            handleNyuk(response);
            return;
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("curly.txt")) {
            if (input == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing resource: curly.txt");
                return;
            }

            response.getWriter().write(new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private void handleNyuk(HttpServletResponse response) throws IOException {
        response.getWriter().write("nyuk");
    }
}
