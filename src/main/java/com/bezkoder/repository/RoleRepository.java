package com.bezkoder.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bezkoder.models.ERole;
import com.bezkoder.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
