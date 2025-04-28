package com.g3.property.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.g3.property.entity.Agent;
import com.g3.property.exception.AgentNotFoundException;
import com.g3.property.repository.AgentRepository;

@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    private AgentRepository agentRepository;
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    // create agent
    @Override
    public Agent createAgent(Agent agent) {
        logger.info("Creating new agent record");
        return agentRepository.save(agent);
    }

    // get one agent
    @Override
    public Agent getAgent(Long id) {
        logger.info("Retrieving details for agent id: {}", id);
        return agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id));
    }

    // update agent
    @Override
    public Agent updateAgent(Long id, Agent updatedFields) {
        Agent agentToUpdate = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id));
        Agent updatedAgent = Agent.builder().id(agentToUpdate.getId())
                                        .firstName(updatedFields.getFirstName() != null ? updatedFields.getFirstName() : agentToUpdate.getFirstName())
                                        .lastName(updatedFields.getLastName() != null ? updatedFields.getLastName() : agentToUpdate.getLastName())
                                        .email(updatedFields.getEmail() != null ? updatedFields.getEmail() : agentToUpdate.getEmail())
                                        .createdAt(agentToUpdate.getCreatedAt())
                                        .build();
        logger.info("Updated details for agent id: {}", id);
        return agentRepository.save(updatedAgent);
    }

    // delete agent
    @Override
    public void deleteAgent(Long id) {
        logger.info("Deleted record for agent id: {}", id);
        agentRepository.deleteById(id);
    }

}
