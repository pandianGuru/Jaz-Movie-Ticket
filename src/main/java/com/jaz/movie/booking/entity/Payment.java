package com.jaz.movie.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    private String bankName;
    private String bankTxnId;
    private String gateWayName;
    private String paymentMode;
    private String responseCode;
    private String responseMess;
    private String status;
    private String transactionDate;
    private String transactionId;
    private String bankTransactionId;
}
