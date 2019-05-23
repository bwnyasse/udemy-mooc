package com.github.bwnyasse.grpc.greeting.client;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Hello Calculator gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // No for PROD
                .build();

        // create the client
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        // SUM
        // build the request
        ToSumRequest request = ToSumRequest.newBuilder().setFirst(3).setSecond(10).build();

        // call The RCP , send the request & get the response
        ToSumResponse response = calculatorClient.sum(request);
        System.out.println(response.getResult());

        channel.shutdown();
    }
}
