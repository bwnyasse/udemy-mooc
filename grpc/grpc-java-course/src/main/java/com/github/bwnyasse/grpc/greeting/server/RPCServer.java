package com.github.bwnyasse.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class RPCServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(" Hello gRPC");

        // Init the server
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();

        // ShutDown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Runnable
            System.out.println("Receive ShutDown Request");
            server.shutdown();
            System.out.println("Succesfully stopped the server");
        }));

        server.awaitTermination();

    }
}
