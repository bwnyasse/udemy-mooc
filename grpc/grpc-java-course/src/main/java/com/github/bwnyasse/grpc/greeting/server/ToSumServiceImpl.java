package com.github.bwnyasse.grpc.greeting.server;

import com.proto.sum.Sum;
import com.proto.sum.ToSumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ToSumServiceImpl extends ToSumServiceGrpc.ToSumServiceImplBase {

    @Override
    public void sum(Sum.ToSumRequest request, StreamObserver<Sum.ToSumResponse> responseObserver) {

        // extract the fields we need
        Sum.ToSum toSum = request.getToSum();
        int first = toSum.getFirst();
        int second = toSum.getSecond();

        // Create the response
        Sum.ToSumResponse response = Sum.ToSumResponse.newBuilder().setResult(first + second).build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC Call
        responseObserver.onCompleted();

        //super.sum(request, responseObserver);
    }
}
