package org.brigero.publish;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public class Publisher {

    private WebSocket webSocket;
    private final String uri;

    public Publisher(String uri) {
        this.uri = uri;
    }

    public void open() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Builder webSocketBuilder = client.newWebSocketBuilder();

            // Manually add headers if needed
            //webSocketBuilder.header("Sec-WebSocket-Extensions", "permessage-deflate");

            webSocket = webSocketBuilder.buildAsync(new URI(uri), new WebSocketListener()).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(Object message, String topic) {
        if (webSocket != null) {
            String msg = topic + " " + new Gson().toJson(message);
            webSocket.sendText(msg, true);
        } else {
            System.out.println("WebSocket is not connected. Message not sent.");
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye").join();
        }
    }

    private static class WebSocketListener implements Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("WebSocket connection opened");
            Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("Received message: " + data);
            return Listener.super.onText(webSocket, data, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("WebSocket connection closed: " + reason);
            return Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            error.printStackTrace();
            Listener.super.onError(webSocket, error);
        }
    }
}