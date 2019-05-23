package com.github.bwnyasse.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.Greet;
import com.proto.greet.GreetServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // not for production
                .build();

        // sync
        System.out.println("Creating stub");

        // OLD & DUMMY
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

        // ******* Start - Do something

        // create the client ( blocking - synchronuous )
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // Build Greeting Message
        Greet.Greeting greeting = Greet.Greeting.newBuilder()
                .setFirstName("Boris-Wilfried")
                .setLastName("Nyasse")
                .build();

        // Build GreetRequest
        Greet.GreetRequest request = Greet.GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // Call the RPC  , Send the request & get the response
        Greet.GreetResponse response = greetClient.greet(request);

        System.out.println(response);
        // ******** End - Do something

        System.out.println("Shutting down the channel");
        channel.shutdown();


    }
}
