package com.github.bwnyasse.grpc.greeting.server;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(ToSumRequest request, StreamObserver<ToSumResponse> responseObserver) {
        int sum = request.getFirst() + request.getSecond();

        ToSumResponse response = ToSumResponse.newBuilder()
                .setResult(sum)
                .build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}
