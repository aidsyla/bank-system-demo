package com.aidsyla.banksystemdemo.models;

import com.aidsyla.banksystemdemo.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Long originatingAccountId;
    private Long resultingAccountId;
    private String transactionReason;
//    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(Long id, BigDecimal amount, Long originatingAccountId, Long resultingAccountId, String transactionReason, TransactionType transactionType) {
        this.id = id;
        this.amount = amount;
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.transactionReason = transactionReason;
        this.transactionType = transactionType;
    }

    public Transaction(BigDecimal amount, Long originatingAccountId, Long resultingAccountId, String transactionReason, TransactionType transactionType) {
        this.amount = amount;
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.transactionReason = transactionReason;
        this.transactionType = transactionType;
    }

    public Transaction() {

    }

    public static void flatFee(Account account) {
        BigDecimal flatFee = new BigDecimal("5.00");
        account.setBalance(account.getBalance().subtract(flatFee));
    }

    public static void percentageFee(Account account, BigDecimal transferAmount, BigDecimal percentageFee1, BigDecimal percentageFee2) {
        account.setBalance(account.getBalance().subtract(transferAmount).subtract(calculateFee(transferAmount, percentageFee1, percentageFee2)));
    }

    public static BigDecimal calculateFee(BigDecimal transferAmount, BigDecimal percentageFee1, BigDecimal percentageFee2) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (transferAmount.compareTo(BigDecimal.ZERO) > 0 && transferAmount.compareTo(BigDecimal.valueOf(5000.00)) < 0) {
            feeAmount = transferAmount.multiply(percentageFee2);
        } else if (transferAmount.compareTo(BigDecimal.valueOf(5000.00)) >= 0) {
            feeAmount = transferAmount.multiply(percentageFee1);
        }
        return feeAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", originatingAccountId=" + originatingAccountId +
                ", resultingAccountId=" + resultingAccountId +
                ", transactionReason='" + transactionReason + '\'' +
                ", transactionType=" + transactionType +
                '}';
    }

    public BigDecimal getAmount() {
        return Account.formatNumber(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Account.formatNumber(amount);
    }

    public long getOriginatingAccountId() {
        return originatingAccountId;
    }

    public void setOriginatingAccountId(Long originatingAccountId) {
        this.originatingAccountId = originatingAccountId;
    }

    public long getResultingAccountId() {
        return resultingAccountId;
    }

    public void setResultingAccountId(Long resultingAccountId) {
        this.resultingAccountId = resultingAccountId;
    }

    public String getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(String transactionReason) {
        this.transactionReason = transactionReason;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}