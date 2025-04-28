package com.g3.property.service;

import com.g3.property.entity.Agent;

public interface AgentService {
    Agent createAgent(Agent agent);
    Agent getAgent(Long id);
    Agent updateAgent(Long Id, Agent agent);
    void deleteAgent(Long id);
}
