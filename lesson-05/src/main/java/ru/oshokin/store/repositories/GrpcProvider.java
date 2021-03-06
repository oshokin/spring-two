package ru.oshokin.store.repositories;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcProvider {

    @Value("${server.grpc.roles.port}")
    private int port;

    public ManagedChannel getConnection() {
       return ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
    }

}
