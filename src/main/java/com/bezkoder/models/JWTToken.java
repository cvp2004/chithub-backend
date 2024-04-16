package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens")
public class JWTToken {

     @Id
     private String id;
     private String token;
     private boolean isValid = true;

     public JWTToken(String token) {
          this.token = token;
     }
}
