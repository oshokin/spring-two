package ru.oshokin.store.interfaces;

import ru.oshokin.store.entities.OutcomingMessageJS;

public interface IWebSocketMessenger {
    void sendMessage(String destination, OutcomingMessageJS message);
}