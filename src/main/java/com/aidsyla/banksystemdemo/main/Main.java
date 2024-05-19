package com.aidsyla.banksystemdemo.main;

import com.aidsyla.banksystemdemo.models.Account;
import com.aidsyla.banksystemdemo.models.Bank;
import com.aidsyla.banksystemdemo.models.Transaction;
import com.aidsyla.banksystemdemo.services.AccountService;
import com.aidsyla.banksystemdemo.services.BankService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.aidsyla.banksystemdemo.utility.Utility.getIntInput;

@Component
public class Main implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankService bankService;

    @Override
    public void run(String... args) throws Exception {
        List<Bank> banks = new ArrayList<>();
        Bank bank = new Bank();
        List<Account> accounts = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {

            System.out.println("Welcome to the Bank System");
            System.out.println("1. Create Bank");
            System.out.println("2. See Banks");
            System.out.println("3. Create Account");
            System.out.println("4. Log in");
            System.out.println("5. Exit");

            int choice = getIntInput(sc);

            switch (choice) {
                case 1:
                    bankService.createBank(sc, banks);
                    break;
                case 2:
                    bankService.listOfBanks(sc);
                    break;
                case 3:
                    accountService.createUser(sc, transactions);
                    break;
                case 4:
                    accountService.handleUserLogin(sc);
                    break;
                case 5:
                    exit = true;
            }
        }
    }
}