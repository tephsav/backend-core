package ru.mentee.power.crm;

import ru.mentee.power.crm.web.HelloCrmServer;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HelloCrmServer server = new HelloCrmServer(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook");
            server.stop();
        }));

        server.start();

        Thread.currentThread().join();
    }
}