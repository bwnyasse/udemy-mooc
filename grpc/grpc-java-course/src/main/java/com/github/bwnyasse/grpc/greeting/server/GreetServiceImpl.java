package com.github.bwnyasse.grpc.greeting.server;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.stub.StreamObserver;

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
}
