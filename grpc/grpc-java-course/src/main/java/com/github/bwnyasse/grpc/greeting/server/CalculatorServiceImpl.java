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

    @Override
    public void primeDecompo(PrimeDecompoRequest request, StreamObserver<PrimeDecompoResponse> responseObserver) {

        primedecompoAlgo(request.getEntry(),responseObserver);
        responseObserver.onCompleted();
    }

    private void primedecompoAlgo(int entry, StreamObserver<PrimeDecompoResponse> responseObserver) {
        int k = 2;
        int N = entry;

        while (N > 1) {
            if (N % k == 0) {
                PrimeDecompoResponse response = PrimeDecompoResponse.newBuilder()
                        .setPrimeFactor(k)
                        .build();
                responseObserver.onNext(response);
                N = N / k;
            } else {
                k++;
            }
        }
    }
}
