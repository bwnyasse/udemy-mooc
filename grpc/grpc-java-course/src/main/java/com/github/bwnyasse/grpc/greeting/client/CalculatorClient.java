package com.github.bwnyasse.grpc.greeting.client;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
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


//        doSumCall(channel);
//        doPrimeDecompoCall(channel);
//        doComputeAverage(channel);
//        doFindMaximum(channel);
        doSquareRootWithErrorCall(channel);
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

        requestObserver.onCompleted();
        ;

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doFindMaximum(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<FindMaximumRequest> requestObserver = asyncClient.findMaximum(new StreamObserver<FindMaximumResponse>() {
            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Response from server : " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                // Make sure the latch disappear
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList(1, 5, 3, 6, 2, 20).forEach(entry -> {
            System.out.println("Sending --> " + entry);
            requestObserver.onNext(FindMaximumRequest.newBuilder()
                    .setEntry(entry)
                    .build());

        });

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSquareRootWithErrorCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        int number = -1;
        try {

            blockingStub.squareRoot(SquareRootRequest.newBuilder().setNumber(number).build());
        }catch(StatusRuntimeException e){

            System.out.println("Got an exception for square root");
            e.printStackTrace();

        }

    }
}
