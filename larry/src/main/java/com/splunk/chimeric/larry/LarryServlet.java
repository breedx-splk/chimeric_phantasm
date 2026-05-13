package com.splunk.chimeric.larry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LarryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("larry.txt")) {
            if (input == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing resource: larry.txt");
                return;
            }

            response.getWriter().write(new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }
}
