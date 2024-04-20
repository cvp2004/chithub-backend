package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "chitgroups")
public class ChitGroup {

     @Id
     private String id;
     @DBRef
     private Agent agent;
     @DBRef
     private List<Member> members;

     private Integer chitValue;
     private Integer noOfMonths;

     private LocalDateTime startDate;

     @DBRef
     private List<Transaction> transactions;
}
