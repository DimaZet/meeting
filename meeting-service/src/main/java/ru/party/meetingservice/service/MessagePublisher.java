package ru.party.meetingservice.service;

public interface MessagePublisher {
    void publish(Object message);
}
