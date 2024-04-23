package com.bezkoder.repository;

import com.bezkoder.models.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;

public interface AuctionRepository extends MongoRepository<String, Auction> {

     List<Auction> findAllByGroupId(String groupId);

     Auction findById(String id);
}
