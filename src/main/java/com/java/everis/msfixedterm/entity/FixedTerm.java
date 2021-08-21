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

    @NotNull
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

}
