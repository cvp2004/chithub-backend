package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "agents")
public class Agent {

     @Id
     private String id;
     private String name;
     private String phoneNo;
     private String username;

     public Agent(String name, String phone) {
          this.setName(name);
          this.setPhoneNo(phone);
     }

}
