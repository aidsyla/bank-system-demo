package com.aidsyla.banksystemdemo.services;

import com.aidsyla.banksystemdemo.models.Account;
import com.aidsyla.banksystemdemo.models.Bank;
import com.aidsyla.banksystemdemo.repository.BankRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static com.aidsyla.banksystemdemo.models.Bank.findBankByName;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Transactional
    public void createBank(Scanner sc, List<Bank> banks) {
        System.out.println("Enter the name of the bank");
        String name = sc.next();

        Bank existingBank = bankRepository.findByName(name);
        if (existingBank != null) {
            System.out.println("Bank already exists");
            System.out.println("Press enter to go back");
            sc.nextLine();
            sc.nextLine();
        } else {
            Bank bank;
            bank = new Bank(name, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            banks.add(bank);
            bankRepository.save(bank);
            System.out.println("Bank created with details: \n" + bank);
        }
    }

    @Transactional
    public void listOfBanks(Scanner sc) {
        System.out.println("All registered banks");
        List<Bank> allBanks = bankRepository.findAll();
        allBanks.forEach(System.out::println);
        boolean searchMode = true;
        while (searchMode) {
            System.out.println("1. Search Bank Accounts");
            System.out.println("2. Exit");
            int choice2 = sc.nextInt();
            switch (choice2) {
                case 1:
                    System.out.println("Enter the name of the bank");
                    String bankName = sc.next();
                    Bank searchedBank = bankRepository.findByName(bankName);
                    assert searchedBank != null;
                    System.out.println(searchedBank.getAccounts());
                    break;
                case 2:
                    searchMode = false;
                    break;
            }
        }
    }
}