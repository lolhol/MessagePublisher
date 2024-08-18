package org.brigero;

import org.brigero.publish.Publisher;

import java.net.URISyntaxException;

public final class MessagePublisher {
    private static Publisher publisher = null;

    public static Publisher create(String targetUrl) throws URISyntaxException {
        publisher = new Publisher(targetUrl);
        try {
            publisher.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publisher;
    }

    public static Publisher create(long port) throws URISyntaxException {
        return create("ws://127.0.0.1:" + port + "/");
    }

    public static boolean publishMessage(Object message, String topic) {
        if (publisher == null) {
            return false;
        }

        publisher.publishMessage(message, topic);
        return true;
    }

    public static boolean close() {
        if (publisher == null) {
            return false;
        }

        publisher.close();
        return true;
    }

    public static boolean open() {
        if (publisher == null) {
            return false;
        }

        publisher.open();
        return true;
    }
}