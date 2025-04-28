package com.g3.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g3.property.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    
}
