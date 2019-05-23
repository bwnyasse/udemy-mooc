package com.github.bwnyasse.grpc.greeting.client;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // not for production
                .build();

        // create the client ( blocking - synchronuous )
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // ******* Start - Do something
        //unaryRPCCall(greetClient);
        serverStreamingRPCCall(greetClient);

        // ******** End - Do something

        System.out.println("Shutting down the channel");
        channel.shutdown();


    }

    private static void unaryRPCCall( GreetServiceGrpc.GreetServiceBlockingStub greetClient) {

        // Build Greeting Message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Boris-Wilfried")
                .setLastName("Nyasse")
                .build();

        // Build GreetRequest
        GreetRequest request = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // Call the RPC  , Send the request & get the response
        GreetResponse response = greetClient.greet(request);

        System.out.println(response);
    }

    private static void serverStreamingRPCCall( GreetServiceGrpc.GreetServiceBlockingStub greetClient) {

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Boris-Wilfried").build())
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(response -> {
                    System.out.println(response.getResult());
                });
    }
}
