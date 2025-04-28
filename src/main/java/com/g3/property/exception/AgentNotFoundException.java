package com.g3.property.exception;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(Long id) {
        super("Could not find agent with id: " + (id));
    }
}
