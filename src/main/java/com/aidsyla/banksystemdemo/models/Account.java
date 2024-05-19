package com.aidsyla.banksystemdemo.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToOne
    private Bank bank;

    public Account(Long id, String username, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.balance = formatNumber(balance);
    }

    public Account(String username, BigDecimal balance, List<Transaction> transactions, Bank bank) {
        this.username = username;
        this.balance = balance;
        this.transactions = transactions;
        this.bank = bank;
    }

    public Account(Long id, String username, BigDecimal balance, List<Transaction> transactions, Bank bank) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.transactions = transactions;
        this.bank = bank;
    }

    public Account() {

    }

    public Account(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }

    public Bank getBank(){
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static Account findAccountByUsername(List<Account> accounts, String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    public static Account findAccountById(List<Account> accounts, int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return formatNumber(balance);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = formatNumber(balance);
    }

    public String getBalanceString() {
        return "$" + balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                ", bank=" + (bank != null ? bank.getName() : "null") +
                '}';
    }

    public static BigDecimal formatNumber(BigDecimal amount){
        return amount.setScale(2, RoundingMode.HALF_DOWN);
    }
}