package com.bezkoder.repository;

import com.bezkoder.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {

     Member findByUsername(String username);
     boolean existsByUsername(String username);
     void deleteByUsername(String username);
}
