package ru.oshokin.store.repositories;

import ru.oshokin.store.RolesRequest;
import ru.oshokin.store.RolesResponse;
import ru.oshokin.store.RolesServiceGrpc;
import ru.oshokin.store.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleGrpcRepository {

    private final GrpcProvider provider;

    public RoleGrpcRepository(@Autowired GrpcProvider provider) {
        this.provider = provider;
    }

    public Role findOneByName(String theRoleName) {
        RolesRequest request = buildMsg(theRoleName);
        RolesServiceGrpc.RolesServiceBlockingStub stub = RolesServiceGrpc.newBlockingStub(provider.getConnection());
        RolesResponse response = stub.getRoles(request);
        return new Role(Long.valueOf(response.getId()), response.getName());
    }

    private RolesRequest buildMsg(String roleName) {
        return RolesRequest.newBuilder()
                .setName(roleName)
                .build();
    }
}
