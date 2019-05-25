package com.github.bwnyasse.grpc.greeting.server;

import com.proto.calculator.*;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

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

        primedecompoAlgo(request.getEntry(), responseObserver);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {

        return new StreamObserver<ComputeAverageRequest>() {

            List<Integer> listOfValues = new ArrayList<>();


            @Override
            public void onNext(ComputeAverageRequest value) {
                listOfValues.add(value.getEntry());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                OptionalDouble optionalDouble = listOfValues.stream().mapToInt(a -> a).average();
                Double average = optionalDouble.isPresent() ? optionalDouble.getAsDouble() : 0;

                responseObserver.onNext(ComputeAverageResponse.newBuilder()
                        .setResult(average)
                        .build());
                responseObserver.onCompleted();
            }
        };
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
