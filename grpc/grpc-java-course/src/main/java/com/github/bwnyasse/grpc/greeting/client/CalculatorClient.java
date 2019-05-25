package com.github.bwnyasse.grpc.greeting.client;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Hello Calculator gRPC client");

        CalculatorClient client = new CalculatorClient();
        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // No for PROD
                .build();


        doSumCall(channel);
        doPrimeDecompoCall(channel);
        doComputeAverage(channel);

        channel.shutdown();
    }

    private void doSumCall(ManagedChannel channel) {

        // create the client
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        int first = 3;
        int second = 10;

        // build the request
        ToSumRequest request = ToSumRequest.newBuilder()
                .setFirst(first)
                .setSecond(second)
                .build();
        System.out.println(" SUM : " + first + " & " + second + " ----");
        // call The RCP , send the request & get the response
        ToSumResponse response = calculatorClient.sum(request);
        System.out.println(response.getResult());
    }

    private void doPrimeDecompoCall(ManagedChannel channel) {

        // create the client
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        int entry = 120;
        PrimeDecompoRequest request = PrimeDecompoRequest.newBuilder()
                .setEntry(120)
                .build();

        System.out.println(" PRIME DECOMPO  of " + entry + "  ----");

        calculatorClient.primeDecompo(request).forEachRemaining(response -> {
            System.out.println(response.getPrimeFactor());
        });


    }

    private void doComputeAverage(ManagedChannel channel) {

        System.out.println(" COMPUTE AVERAGE  ----");

        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                // We get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        System.out.println("Entries are : ");
        for (int i = 1; i < 5; i++) {
            System.out.println(i);
            requestObserver.onNext(ComputeAverageRequest.newBuilder().setEntry(i).build());
        }

        requestObserver.onCompleted();;

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
