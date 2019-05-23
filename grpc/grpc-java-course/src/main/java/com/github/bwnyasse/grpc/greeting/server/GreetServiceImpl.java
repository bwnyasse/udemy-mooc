package com.github.bwnyasse.grpc.greeting.server;

import com.proto.greet.Greet;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

    @Override
    public void greet(Greet.GreetRequest request, StreamObserver<Greet.GreetResponse> responseObserver) {

        // extract the fields we need
        final Greet.Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        // create the response
        String result = "Hello " + firstName;
        Greet.GreetResponse response = Greet.GreetResponse.newBuilder()
                .setResult(result)
                .build();

        // Send the response
        responseObserver.onNext(response);

        // complete the RPC Call
        responseObserver.onCompleted();
        
        //super.greet(request, responseObserver);
    }
}
