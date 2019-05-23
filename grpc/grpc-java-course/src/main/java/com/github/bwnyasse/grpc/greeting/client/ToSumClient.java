package com.github.bwnyasse.grpc.greeting.client;

import com.proto.sum.Sum;
import com.proto.sum.ToSumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ToSumClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a Sum API gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // No for PROD
                .build();

        // create the client
        ToSumServiceGrpc.ToSumServiceBlockingStub toSumClient = ToSumServiceGrpc.newBlockingStub(channel);

        // Build the message
        Sum.ToSum toSum = Sum.ToSum.newBuilder()
                .setFirst(3)
                .setSecond(10)
                .build();

        // build the request
        Sum.ToSumRequest request = Sum.ToSumRequest.newBuilder()
                .setToSum(toSum)
                .build();

        // call The RCP , send the request & get the response
        Sum.ToSumResponse response = toSumClient.sum(request);
        System.out.println(response);

        channel.shutdown();


    }
}


