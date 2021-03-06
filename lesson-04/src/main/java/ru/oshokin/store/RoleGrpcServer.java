package ru.oshokin.store;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RoleGrpcServer {

    public RoleGrpcServer(@Autowired RolesServiceImpl rolesService, @Value("${server.grpc.roles.port}") int port) {
        try {
            rolesService.startServer(port);
        } catch (Exception e) {
            throw new BeanInitializationException("Couldn't start server", e);
        }
    }

}