package com.aidsyla.banksystemdemo.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;

    private BigDecimal totalTransactionFeeAmount = BigDecimal.ZERO;
    private BigDecimal totalTransferAmount = BigDecimal.ZERO;
    private BigDecimal flatFee = BigDecimal.ZERO;
    private BigDecimal percentageFee = BigDecimal.ZERO;

    public Bank() {

    }

    public Bank(String name, BigDecimal totalTransactionFeeAmount, BigDecimal totalTransferAmount, BigDecimal flatFee, BigDecimal percentageFee) {
        this.name = name;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.flatFee = flatFee;
        this.percentageFee = percentageFee;
    }

    public Bank(String bank) {

    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accounts=" + accounts +
                ", totalTransactionFeeAmount=" + totalTransactionFeeAmount +
                ", totalTransferAmount=" + totalTransferAmount +
                ", flatFee=" + flatFee +
                ", percentageFee=" + percentageFee +
                '}';
    }

    public Bank(String name, List<Account> accounts, BigDecimal totalTransactionFeeAmount, BigDecimal totalTransferAmount, BigDecimal flatFee, BigDecimal percentageFee) {
        this.name = name;
        this.accounts = accounts;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.flatFee = flatFee;
        this.percentageFee = percentageFee;
    }

    public static Bank findBankByName(List<Bank> banks, String name) {
        for (Bank bank : banks) {
            if (bank.getName().equalsIgnoreCase(name)) {
                return bank;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public BigDecimal getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public void setTotalTransactionFeeAmount(BigDecimal totalTransactionFeeAmount) {
        this.totalTransactionFeeAmount = Account.formatNumber(totalTransactionFeeAmount);
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(BigDecimal flatFee) {
        this.flatFee = flatFee;
    }

    public BigDecimal getPercentFee() {
        return percentageFee;
    }

    public void setPercentFee(BigDecimal percentageFee) {
        this.percentageFee = percentageFee;
    }
}
