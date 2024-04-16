package com.bezkoder.repository;

import com.bezkoder.models.Agent;
import com.bezkoder.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AgentRepository extends MongoRepository<Agent, String> {
     Agent findByUsername(String username);
     boolean existsByUsername(String username);
     void deleteAgentByUsername(String username);
}
