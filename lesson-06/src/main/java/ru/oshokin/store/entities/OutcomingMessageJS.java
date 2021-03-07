package ru.oshokin.store.entities;

public class OutcomingMessageJS {

    private int id;
    private String content;

    public OutcomingMessageJS(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}