package ru.oshokin.store;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.oshokin.store.RolesServiceGrpc.RolesServiceImplBase;
import ru.oshokin.store.entities.Role;
import ru.oshokin.store.repositories.RoleRepository;

import java.io.IOException;

@Component
public class RolesServiceImpl extends RolesServiceImplBase {

    RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Async
    public void startServer(int port)  throws IOException, InterruptedException {
        //1. Доработать портал и с помощью grpc передавать роли пользователя.
        //Ужасный overhead, имхо. Сервер общается сам с собой, лол кек?
        Server server = ServerBuilder.forPort(port).addService(this).build();
        server.start();
        server.awaitTermination();
    }

    @Override
    public void getRoles(RolesRequest request, StreamObserver<RolesResponse> responseObserver) {
        Role foundRole = roleRepository.findOneByName(request.getName());
        RolesResponse.Builder roleBuilder = RolesResponse.newBuilder();
        RolesResponse response = roleBuilder.setId(0).build();
        if (foundRole != null) response = roleBuilder.setId(foundRole.getId().intValue()).setName(foundRole.getName()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}