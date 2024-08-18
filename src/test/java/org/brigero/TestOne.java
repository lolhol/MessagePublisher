package org.brigero;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestOne {
    static class TestClass {
        public int a = 1;
        public boolean b = false;

        public TestClass(int a, boolean b) {
            this.a = a;
            this.b = b;
        }
    }

    @Test
    public void testOne() throws IOException, URISyntaxException, InterruptedException {
        MessagePublisher.create(8081);
        Thread.sleep(1000);
        MessagePublisher.publishMessage(new TestClass(1, true), "ExampleRoute");
    }
}
