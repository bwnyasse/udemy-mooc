package com.github.bwnyasse.grpc.greeting.client;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Hello Calculator gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // No for PROD
                .build();

        // create the client
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
        sumRPCCall(calculatorClient);
        primedecompoRPCCall(calculatorClient);

        channel.shutdown();
    }

    private static void sumRPCCall(CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient) {

        int first = 3;
        int second = 10;

        // build the request
        ToSumRequest request = ToSumRequest.newBuilder()
                .setFirst(first)
                .setSecond(second)
                .build();
        System.out.println(" SUM : " + first +" & " + second + " ----");
        // call The RCP , send the request & get the response
        ToSumResponse response = calculatorClient.sum(request);
        System.out.println(response.getResult());
    }

    private static void primedecompoRPCCall(CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient) {

        int entry = 120;
        PrimeDecompoRequest request = PrimeDecompoRequest.newBuilder()
                .setEntry(120)
                .build();

        System.out.println(" PRIME DECOMPO  of " + entry + "  ----");

        calculatorClient.primeDecompo(request).forEachRemaining(response -> {
            System.out.println(response.getPrimeFactor());
        });


    }
}
