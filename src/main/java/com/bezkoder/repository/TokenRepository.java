package com.bezkoder.repository;

import com.bezkoder.models.JWTToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<JWTToken, String> {

     public JWTToken findTokenByToken(String token);

}
