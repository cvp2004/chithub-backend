package com.bezkoder.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reports")
public class Report {
     @Id
     private String id;
     @NonNull
     private String groupId;
     @NonNull
     private Integer installment;
     @NonNull
     private Integer noOfMembers;
     @NonNull
     private Integer totalAmtCollected;
     @NonNull
     private Integer lastMonthDividend;
     @NonNull
     private Integer totalAmtForAuction;
     @NonNull
     private String username;
     @NonNull
     private Integer bidOffer;
     @NonNull
     private Integer foremanCommission;
     @NonNull
     private Integer netPayable;
     @NonNull
     private Integer dividendTotal;
     @NonNull
     private Integer dividendPerHead;
     @NonNull
     private Integer nextMonthInstallment;
}
