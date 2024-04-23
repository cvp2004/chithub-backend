package com.bezkoder.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "groups")
public class Group {

     @Id
     private String id;
     @DBRef
     @NotEmpty
     private List<Member> members;
     @DBRef
     private Agent agent;
     @NonNull
     @DBRef
     private List<Auction> auctions;
     private Integer noOfMonths;
     @DBRef
     private List<Report> reports;
}
