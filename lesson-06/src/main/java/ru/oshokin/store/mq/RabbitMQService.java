package ru.oshokin.store.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Data
@Component
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true")
public class RabbitMQService implements MessageQueuingService {

    private String URI;

    private String exchange;

    private String queue;

    private ConnectionFactory factory;

    private Connection connection;

    private Map<Thread, Channel> pool = new HashMap<>(1);

    @Autowired
    private ApplicationContext ctx;

    public RabbitMQService(@Value("${rabbitmq.uri}") String URI, @Value("${rabbitmq.exchange}") String exchange, @Value("${rabbitmq.queue}") String queue) {
        this.URI = URI;
        this.exchange = exchange;
        this.queue = queue;
    }

    @PostConstruct
    public void start() {
        try {
            factory = new ConnectionFactory();
            factory.setUri(URI);
            connection = factory.newConnection("app:oshokin-store");
            Channel channel = getChannel();
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, queue);
        } catch (Throwable t) {
            log.error(t.getMessage());
            SpringApplication.exit(ctx, () -> 1);
        }
        log.info("RabbitMQ service started successfully");
    }

    @PreDestroy
    public void stop() {
        pool.forEach((thread, channel) -> {
            try {
                if (channel != null && channel.isOpen()) channel.close();
            } catch (IOException | TimeoutException e) {
                log.error("А кто это вот такое сделал (c) Бородач ?!");
                log.error(e.getMessage());
            }
        });
        if (connection != null && connection.isOpen())
            try {
                connection.close();
            } catch (IOException e) {
                log.error("Понять и простить (с) Бородач ?");
                log.error(e.getMessage());
            }
        log.info("RabbitMQ service stopped successfully");
    }

    public void sendMessage(String message) throws IOException {
        sendMessage(exchange, queue, message);
    }

    public void sendMessage(String exchange, String queue, String message) throws IOException {
        Channel channel = getChannel();
        channel.basicPublish(exchange, queue, null, message.getBytes(StandardCharsets.UTF_8));
    }

    public boolean hasNext() throws IOException {
        return hasNext(queue);
    }

    public boolean hasNext(String queue) throws IOException {
        Channel channel = getChannel();
        return (channel.isOpen() && channel.messageCount(queue) > 0);
    }

    public String getMessage() throws IOException {
        return getMessage(queue, true);
    }

    public String getMessage(String queue, boolean autoAck) throws IOException {
        Channel channel = getChannel();
        return new String(channel.basicGet(queue, autoAck).getBody(), StandardCharsets.UTF_8);
    }

    private Channel getChannel() throws IOException {
        Thread currentThread = Thread.currentThread();
        Channel channel = pool.get(currentThread);
        if (channel == null) {
            channel = connection.createChannel();
            pool.put(currentThread, channel);
        }
        return channel;
    }

}
