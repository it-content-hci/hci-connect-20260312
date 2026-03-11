package ch.documedis.handson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Tiny embedded HTTP server that serves the webview/dist files from classpath resources.
 * Needed because JavaFX WebView loading from file:// triggers CORS blocks on API calls.
 */
public class WebviewServer {

    private static final Map<String, String> MIME_TYPES = Map.of(
            "html", "text/html; charset=utf-8",
            "js", "application/javascript; charset=utf-8",
            "css", "text/css; charset=utf-8",
            "json", "application/json; charset=utf-8",
            "png", "image/png",
            "svg", "image/svg+xml"
    );

    private final HttpServer server;
    private final int port;

    private static final int PORT = 5173;

    public WebviewServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        port = server.getAddress().getPort();
        server.createContext("/", this::handleRequest);
        server.setExecutor(null);
    }

    public void start() {
        server.start();
        System.out.println("[WebviewServer] Serving on http://localhost:" + port);
    }

    public void stop() {
        server.stop(0);
    }

    public String getUrl() {
        return "http://localhost:" + port + "/index.html";
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";

        String resourcePath = "/webview" + path;
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                String notFound = "Not found: " + path;
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes());
                }
                return;
            }

            byte[] data = is.readAllBytes();
            String ext = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : "";
            String contentType = MIME_TYPES.getOrDefault(ext, "application/octet-stream");

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        }
    }
}
