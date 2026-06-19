package org.sifenboot.app.shared.htmx;

import jakarta.servlet.http.HttpServletResponse;

public final class HtmxUtils {

    private HtmxUtils() {
    }

    public static void success(
            HttpServletResponse response,
            String message) {

        response.setHeader(
                "HX-Trigger",
                "{\"showAlert\":{\"message\":\""
                        + message
                        + "\",\"type\":\"success\"}}"
        );
    }

    public static void error(
            HttpServletResponse response,
            String message) {

        response.setHeader(
                "HX-Trigger",
                "{\"showAlert\":{\"message\":\""
                        + message
                        + "\",\"type\":\"error\"}}"
        );
    }

    public static void info(
            HttpServletResponse response,
            String message) {

        response.setHeader(
                "HX-Trigger",
                "{\"showAlert\":{\"message\":\""
                        + message
                        + "\",\"type\":\"info\"}}"
        );
    }

    public static void warning(
            HttpServletResponse response,
            String message) {

        response.setHeader(
                "HX-Trigger",
                "{\"showAlert\":{\"message\":\""
                        + message
                        + "\",\"type\":\"warning\"}}"
        );
    }
}