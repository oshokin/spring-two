package ru.oshokin.store.mq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Data
@Component
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "false")
public class DummyMQService implements MessageQueuingService {

    @Override
    @PostConstruct
    public void start() {
        log.info("DummyMQ service started successfully");
    }

    @Override
    @PreDestroy
    public void stop() {
        log.info("DummyMQ service stopped successfully");
    }

    @Override
    public boolean hasNext() throws IOException {
        return false;
    }

    @Override
    public void sendMessage(String message) throws IOException {
        log.info("DummyMQ service sent message: {}", message);
    }

    @Override
    public String getMessage() throws IOException {
        log.info("DummyMQ service just received message");
        return "Holitas, mi querido usuario!";
    }

}
