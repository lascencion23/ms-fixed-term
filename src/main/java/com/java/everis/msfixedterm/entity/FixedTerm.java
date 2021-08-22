package com.java.everis.msfixedterm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Data
@Builder
@Document("FixedTerm")
@AllArgsConstructor
@NoArgsConstructor
public class FixedTerm {
    @Id
    private String id;

    @NotNull
    private Customer customer;

    private String accountNumber;

    private List<Person> holders;

    private List<Person> signers;

    @NotNull
    private Double balance;

    private Integer limitDeposits;

    private Integer limitDraft;

    @NotNull
    private LocalDate allowDateTransaction;

    @NotNull
    private Integer freeTransactions;

    @NotNull
    private Double commissionTransactions;

    private LocalDateTime date;

    public static String generateAccountNumber() {
        final String ACCOUNT_PREFIX = "200-";
        Random random = new Random();
        return ACCOUNT_PREFIX + random.nextInt(999999999);
    }

}
