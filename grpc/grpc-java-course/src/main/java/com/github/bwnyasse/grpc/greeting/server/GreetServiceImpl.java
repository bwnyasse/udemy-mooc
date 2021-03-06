package com.github.bwnyasse.grpc.greeting.server;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

import java.util.stream.Stream;

public class GreetServiceImpl extends GreetServiceImplBase {

    //@Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        // extract the fields we need
        final Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        // create the response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        // Send the response
        responseObserver.onNext(response);

        // complete the RPC Call
        responseObserver.onCompleted();

        //super.greet(request, responseObserver);
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {


        try {
            String firstName = request.getGreeting().getFirstName();

            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstName + ", response number : " + i;

                // create many response
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();

        }
        //super.greetManyTimes(request, responseObserver);
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> streamObserverOfRequest = new StreamObserver<LongGreetRequest>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                // Client sends a message

                // Everything we receive message from client, we add it in the result
                result += " Hello " + value.getGreeting().getFirstName() + " | ";
            }

            @Override
            public void onError(Throwable t) {
                // Client sends an error
            }

            @Override
            public void onCompleted() {
                // client is done
                // This is when we want to return a response ( responseObserver )
                responseObserver.onNext(
                        LongGreetResponse
                                .newBuilder()
                                .setResult(result)
                                .build());
                responseObserver.onCompleted();
                ;
            }
        };
        return streamObserverOfRequest;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {

        StreamObserver<GreetEveryoneRequest> streamObserver = new StreamObserver<GreetEveryoneRequest>() {

            @Override
            public void onNext(GreetEveryoneRequest value) {
                String result = "Hello " + value.getGreeting().getFirstName();
                GreetEveryoneResponse response = GreetEveryoneResponse.newBuilder().setResult(result).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                // do nothing
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
                ;
            }
        };

        return streamObserver;
    }

    @Override
    public void greetWithDeadLine(GreetWithDeadLineRequest request, StreamObserver<GreetWithDeadLineResponse> responseObserver) {

        Context current = Context.current();
        try {
            for (int i = 0; i < 3; i++) {

                if (current.isCancelled()) {
                    return;
                }
                System.out.println("sleep for 100ms");
                Thread.sleep(100);

            }

            System.out.println("Send Response");
            responseObserver.onNext(GreetWithDeadLineResponse.newBuilder()
                    .setResult("Hello " + request.getGreeting().getFirstName())
                    .build());

            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
