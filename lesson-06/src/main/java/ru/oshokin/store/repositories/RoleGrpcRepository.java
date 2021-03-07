package ru.oshokin.store.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.oshokin.store.RolesRequest;
import ru.oshokin.store.RolesResponse;
import ru.oshokin.store.RolesServiceGrpc;
import ru.oshokin.store.entities.Role;

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
        Role funcResult = null;
        if (response.getId() > 0) funcResult = new Role(Long.valueOf(response.getId()), response.getName());
        return funcResult;
    }

    private RolesRequest buildMsg(String roleName) {
        return RolesRequest.newBuilder().setName(roleName).build();
    }

}
