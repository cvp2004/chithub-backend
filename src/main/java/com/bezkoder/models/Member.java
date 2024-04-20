package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value="members")
public class Member {

     @Id
     private String id;
     private String name;
     private String phoneNo;
     private String username;

     public Member(String name, String phoneNo, String username) {
          this.name = name;
          this.phoneNo = phoneNo;
          this.username = username;
     }
}
