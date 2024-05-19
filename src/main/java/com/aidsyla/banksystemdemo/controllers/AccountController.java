package com.aidsyla.banksystemdemo.controllers;

import com.aidsyla.banksystemdemo.models.Account;
import com.aidsyla.banksystemdemo.models.Transaction;
import com.aidsyla.banksystemdemo.repository.AccountRepository;
import com.aidsyla.banksystemdemo.repository.TransactionRepository;
import com.aidsyla.banksystemdemo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping("/createUser")
    public void createUser(@RequestBody Scanner sc, List<Transaction> transactions) {
        accountService.createUser(sc, transactions);
    }

    @PostMapping("/login")
    public void handleUserLogin(@RequestBody Scanner sc) {
        accountService.handleUserLogin(sc);
    }

    @GetMapping("/checkBalance")
    public void checkBalance(@RequestBody Scanner sc) {
        accountService.checkBalance(sc);
    }

    @PutMapping("/withdrawMoney")
    public void withdrawMoney(@RequestBody Scanner sc) {
        accountService.withdrawMoney(sc);
    }

    @PutMapping("/depositMoney")
    public void depositMoney(@RequestBody Scanner sc) {
        accountService.depositMoney(sc);
    }

    @PutMapping("/transferTo")
    public void transferTo(@RequestBody Scanner sc) {
        accountService.transferTo(sc);
    }

    @GetMapping("/transferHistory")
    public void transferHistory(@RequestBody Account loggedInAccount, Scanner sc) {
        accountService.transferHistory(loggedInAccount, transactionRepository,sc);
    }

    @GetMapping("/showTotalTransactionDetails")
    public void showTotalTransactionDetails() {
        accountService.showTotalTransactionDetails();
    }
}