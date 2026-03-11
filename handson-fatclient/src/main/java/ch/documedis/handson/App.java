package ch.documedis.handson;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Hands-on JavaFX fat client demonstrating @documedis/web-components
 * integration via WebView — ProductDetails component.
 */
public class App extends Application {

    private WebEngine webEngine;
    private WebviewServer webviewServer;

    @Override
    public void start(Stage stage) {
        // ─── Top toolbar ───
        Label productLabel = new Label("Product #:");
        TextField productInput = new TextField("2525");
        productInput.setPrefWidth(120);
        Button loadButton = new Button("Load");

        HBox toolbar = new HBox(10, productLabel, productInput, loadButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(8, 12, 8, 12));
        toolbar.setStyle("-fx-background-color: #f0f2f5; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");

        // ─── Center: WebView ───
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.setOnAlert(event -> System.out.println("[WebView] " + event.getData()));

        // Serve webview via embedded HTTP server
        try {
            webviewServer = new WebviewServer();
            webviewServer.start();
            webEngine.load(webviewServer.getUrl());
        } catch (java.io.IOException ex) {
            System.err.println("[App] Failed to start embedded server: " + ex.getMessage());
            java.net.URL builtIndex = getClass().getResource("/webview/index.html");
            if (builtIndex != null) {
                webEngine.load(builtIndex.toExternalForm());
            }
        }

        // ─── Toolbar actions ───
        loadButton.setOnAction(e -> {
            try {
                int productNumber = Integer.parseInt(productInput.getText().trim());
                safeExecuteScript("window.setProductNumber(" + productNumber + ")");
            } catch (NumberFormatException ignored) {
                productInput.setStyle("-fx-border-color: red;");
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
                pause.setOnFinished(ev -> productInput.setStyle(""));
                pause.play();
            }
        });
        productInput.setOnAction(e -> loadButton.fire());

        // ─── Layout ───
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Documedis Components — Hands-on Fat Client");
        stage.setScene(scene);
        stage.show();
    }

    private void safeExecuteScript(String script) {
        try {
            webEngine.executeScript(script);
        } catch (netscape.javascript.JSException ex) {
            System.err.println("[App] JS call skipped: " + ex.getMessage());
        }
    }

    @Override
    public void stop() {
        if (webviewServer != null) {
            webviewServer.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
