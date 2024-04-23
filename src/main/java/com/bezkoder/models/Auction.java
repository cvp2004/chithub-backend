package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

     @Id
     @NonNull
     private String id;
     @NonNull
     private String groupId;
     @NonNull
     private LocalDateTime dateTime;
     @NonNull
     private Integer winningBid;
     @NonNull
     private String winnerUsername;
}

