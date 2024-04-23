package com.bezkoder.controllers;

import com.bezkoder.models.Auction;
import com.bezkoder.payload.response.MessageResponse;
import com.bezkoder.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auction/data")
public class AuctionDataController {

     private final AuctionRepository auctionRepository;
     @Autowired
     public AuctionDataController(AuctionRepository auctionRepository) {
          this.auctionRepository = auctionRepository;
     }

     @GetMapping("/{id}")
     @PreAuthorize("hasRole('MEMBER') or hasRole('AGENT')")
     public ResponseEntity<?> getAuctionDetails(@PathVariable String id) {
          Auction auction = auctionRepository.findById(id);

          if(auction == null)
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse("No Auction Details Found !!"));

          return ResponseEntity
                  .ok(auction);
     }

     @GetMapping("/group/{groupid}")
     @PreAuthorize("hasRole('AGENT') OR hasRole('MEMBER')")
     public ResponseEntity<?> getAllAuctionsByGroupId(String groupid) {
          List<Auction> auctions = auctionRepository.findAllByGroupId(groupid);

          if(auctions.isEmpty())
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse("No Auction Details found !!"));

          return ResponseEntity
                  .ok(auctions);
     }
}
