package com.bezkoder.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value="transactions")
public class Transaction {
     @Id
     private String id;
     @NotNull
     private String groupId;
     @Positive
     private Integer amount;
     @DBRef
     @NotBlank
     private String sender; // Username
     @DBRef
     @NotBlank
     private String receiver; // Username
     private LocalDateTime timestamp = LocalDateTime.now();

     public Transaction(String groupId, Integer amount, String sender, String receiver) {
          this.groupId = groupId;
          this.amount = amount;
          this.sender = sender;
          this.receiver = receiver;
     }
}