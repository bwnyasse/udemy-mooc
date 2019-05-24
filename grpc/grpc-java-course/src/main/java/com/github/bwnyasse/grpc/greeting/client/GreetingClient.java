package com.github.bwnyasse.grpc.greeting.client;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        GreetingClient main = new GreetingClient();
        main.run();

    }

    private void run() {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext() // not for production
                .build();


        // ******* Start - Do something
        doUnaryCall(channel);
        doServerStreamingCall(channel);
        doClientStreamingCall(channel);
        // ******** End - Do something

        System.out.println("Shutting down the channel");
        channel.shutdown();
    }


    private void doUnaryCall(ManagedChannel channel) {

        // create the client ( blocking - synchronuous )
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        System.out.println("------ UNARY CALL ---------");
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

    private void doServerStreamingCall(ManagedChannel channel) {

        // create the client ( blocking - synchronuous )
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        System.out.println("------ SERVER STREAMING CALL ---------");
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Boris-Wilfried").build())
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(response -> {
                    System.out.println(response.getResult());
                });
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // We get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        System.out.println("Sending message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Bob")
                        .build())
                .build());

        System.out.println("Sending message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Paul")
                        .build())
                .build());

        System.out.println("Sending message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Tanguy")
                        .build())
                .build());

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
