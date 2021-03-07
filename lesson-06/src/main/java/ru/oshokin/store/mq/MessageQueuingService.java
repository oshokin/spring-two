package ru.oshokin.store.mq;

import java.io.IOException;

public interface MessageQueuingService {

    public void start();
    public void stop();
    public boolean hasNext() throws IOException;
    public void sendMessage(String message)  throws IOException;
    public String getMessage() throws IOException;

}
