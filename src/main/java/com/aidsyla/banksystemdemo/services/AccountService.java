package com.aidsyla.banksystemdemo.services;

import com.aidsyla.banksystemdemo.enums.TransactionType;
import com.aidsyla.banksystemdemo.models.Account;
import com.aidsyla.banksystemdemo.models.Bank;
import com.aidsyla.banksystemdemo.models.Transaction;
import com.aidsyla.banksystemdemo.repository.AccountRepository;
import com.aidsyla.banksystemdemo.repository.BankRepository;
import com.aidsyla.banksystemdemo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.aidsyla.banksystemdemo.models.Account.*;
import static com.aidsyla.banksystemdemo.models.Transaction.flatFee;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankRepository bankRepository;

    private String loggedInUsername;
    private BigDecimal totalTransactionFeeAmount = BigDecimal.ZERO;
    private BigDecimal totalTransferAmount = BigDecimal.ZERO;

    @Transactional
    public void createUser(Scanner sc, List<Transaction> transactions) {
        if (bankRepository.findAll().isEmpty()) {
            System.out.println("You need to create a Bank first");
            System.out.println("Press enter to go back");
            sc.nextLine();
            sc.nextLine();
            return;
        }

        System.out.println("Enter your username");
        String username = sc.next();

        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            System.out.println("Username already exists. Please choose a different username.");
            System.out.println("Press enter to go back");
            sc.nextLine();
            sc.nextLine();
            return;
        }

        System.out.println("Enter your balance");
        BigDecimal balance = sc.nextBigDecimal();
        assert balance != null;

        System.out.println("Banks you can register on: ");
        List<Bank> allBanks = bankRepository.findAll();
        allBanks.forEach(System.out::println);

        System.out.println("Enter bank name or bank id");
        String input = sc.next();

        Optional<Bank> bankOptional = isNumeric(input) ?
                bankRepository.findById(Long.parseLong(input)) :
                Optional.ofNullable(bankRepository.findByName(input));

        if (bankOptional.isPresent()) {
            Bank bank = bankOptional.get();
            Account account = new Account(username, balance, transactions, bank);
            bank.getAccounts().add(account);

            accountRepository.save(account);
            bankRepository.save(bank);

            System.out.println("Account created with details: " + account);
        } else {
            System.out.println("Bank with name/ID " + input + " not found.");
        }
    }

    public void handleUserLogin(Scanner sc) {
        System.out.println("Enter your username to login");
        String username = sc.next();
        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        if (accountOptional.isEmpty()) {
            System.out.println("User does not exist");
        } else {
            Account account = accountOptional.get();
            System.out.println("Logged in");
            loggedInUsername = username;
            loginScreen(account);
        }
    }

    @Transactional
    public void checkBalance(Scanner sc) {
        Optional<Account> accountOptional = accountRepository.findByUsername(loggedInUsername);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            System.out.println("Your balance is: $" + account.getBalance());
        } else {
            System.out.println("Account with username " + sc + " not found.");
        }
    }

    @Transactional
    public void withdrawMoney(Scanner sc) {

        Optional<Account> account = accountRepository.findByUsername(loggedInUsername);
        if (account.isEmpty()) {
            System.out.println("Account not found!");
            return;
        }

        System.out.println("Enter the amount to withdraw:");
        BigDecimal amount = sc.nextBigDecimal();
        sc.nextLine();

        if (account.get().getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Insufficient balance!");
            return;
        }

        account.get().setBalance(account.get().getBalance().subtract(amount));
        flatFee(account.get());
        addTransferAmount(amount);
        addTransactionFee(BigDecimal.valueOf(5.00));
        accountRepository.save(account.get());

        Bank bank = bankRepository.findByName(account.get().getBank().getName());
        if (bank == null) {
            System.out.println("Bank not found!");
            return;
        }

        Transaction transaction = new Transaction(
                amount, account.get().getId(),
                account.get().getId(), "Other", TransactionType.WITHDRAWAL
        );
        transactionRepository.save(transaction);
        bankRepository.save(bank);

        System.out.println("Withdrawal successful! Your balance is: $" + account.get().getBalance());
    }

    @Transactional
    public void depositMoney(Scanner sc) {

        Optional<Account> account = accountRepository.findByUsername(loggedInUsername);
        if (account.isEmpty()) {
            System.out.println("Account not found!");
            return;
        }

        System.out.println("Enter the amount to deposit:");
        BigDecimal amount = sc.nextBigDecimal();
        sc.nextLine();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid deposit amount!");
            return;
        }

        account.get().setBalance(account.get().getBalance().add(amount));
        accountRepository.save(account.get());
        addTransferAmount(amount);

        Bank bank = bankRepository.findByName(account.get().getBank().getName());
        if (bank == null) {
            System.out.println("Bank not found!");
            return;
        }

        Transaction transaction = new Transaction(
                amount, account.get().getId(),
                account.get().getId(), "Other", TransactionType.DEPOSIT
        );
        transactionRepository.save(transaction);
        bankRepository.save(bank);

        System.out.println("Deposit successful! Your balance is: $" + account.get().getBalance());
    }

    @Transactional
    public void transferTo(Scanner sc) {

        Optional<Account> fromAccount = accountRepository.findByUsername(loggedInUsername);
        if (fromAccount.isEmpty()) {
            System.out.println("Account not found!");
            return;
        }

        List<Account> allAccounts = accountRepository.findAll();
        if (allAccounts.isEmpty()) {
            System.out.println("No accounts available to transfer to!");
            return;
        }

        System.out.println("Available accounts to transfer to:");
        for (Account account : allAccounts) {
            if (!account.getUsername().equals(loggedInUsername)) {
                System.out.println("Username: " + account.getUsername() + ", Balance: $" + account.getBalance());
            }
        }

        System.out.println("Enter the recipient's username:");
        String recipientUsername = sc.next();
        sc.nextLine();
        Optional<Account> toAccount = accountRepository.findByUsername(recipientUsername);
        if (toAccount.isEmpty()) {
            System.out.println("Recipient account not found!");
            return;
        }

        System.out.println("Enter the amount to transfer:");
        BigDecimal amount = sc.nextBigDecimal();
        sc.nextLine();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid transfer amount!");
            return;
        }

        BigDecimal feePercentage;
        if (amount.compareTo(new BigDecimal("1000")) <= 0) {
            feePercentage = new BigDecimal("0.025");
        } else {
            feePercentage = new BigDecimal("0.035");
        }

        BigDecimal fee = amount.multiply(feePercentage);
        BigDecimal totalAmount = amount.add(fee);

        if (fromAccount.get().getBalance().compareTo(totalAmount) < 0) {
            System.out.println("Insufficient balance!");
            return;
        }

        fromAccount.get().setBalance(fromAccount.get().getBalance().subtract(totalAmount));
        toAccount.get().setBalance(toAccount.get().getBalance().add(amount));
        addTransferAmount(amount);

        accountRepository.save(fromAccount.get());
        accountRepository.save(toAccount.get());

        Bank fromBank = bankRepository.findByName(fromAccount.get().getBank().getName());
        Bank toBank = bankRepository.findByName(toAccount.get().getBank().getName());
        if (fromBank == null || toBank == null) {
            System.out.println("Bank not found!");
            return;
        }

        Transaction transaction = new Transaction(
                amount, fromAccount.get().getId(),
                toAccount.get().getId(), "Transfer", TransactionType.TRANSFER
        );
        transactionRepository.save(transaction);
        bankRepository.save(fromBank);
        bankRepository.save(toBank);

        System.out.println("Transfer successful! Your balance is: $" + fromAccount.get().getBalance());
        System.out.println("Transfer fee applied: $" + fee);
    }

    @Transactional
    public void transferHistory(Account loggedInAccount, TransactionRepository transactionRepository, Scanner sc) {
        System.out.println("Here is your transfer history:");

        List<Transaction> transferList = transactionRepository
                .findByOriginatingAccountIdOrResultingAccountId(loggedInAccount.getId(), loggedInAccount.getId());

        if (transferList.isEmpty()) {
            System.out.println("No transfer history found.");
        } else {
            transferList.forEach(System.out::println);
        }

        System.out.println("Press enter to exit");
        sc.nextLine();
        sc.nextLine();
    }

    public void addTransactionFee(BigDecimal fee) {
        this.totalTransactionFeeAmount = this.totalTransactionFeeAmount.add(fee);
    }

    public void addTransferAmount(BigDecimal amount) {
        this.totalTransferAmount = this.totalTransferAmount.add(amount);
    }

    public void showTotalTransactionDetails() {
        System.out.println("Total Transaction Fees: $" + totalTransactionFeeAmount);
        System.out.println("Total Transfer Amounts: $" + totalTransferAmount);
    }

    public void loginScreen(Account loggedInAccount) {
        List<Transaction> transactions = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = true;
        while (loggedIn) {

            System.out.println("Welcome to our home page");
            System.out.println("1. Check balance");
            System.out.println("2. Withdraw money");
            System.out.println("3. Deposit money");
            System.out.println("4. Transfer to another account");
            System.out.println("5. See transaction history");
            System.out.println("6. See all transaction amounts");
            System.out.println("7. Log out");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    checkBalance(sc);
                    break;
                case 2:
                    withdrawMoney(sc);
                    break;
                case 3:
                    depositMoney(sc);
                    break;
                case 4:
                    transferTo(sc);
                    break;
                case 5:
                    transferHistory(loggedInAccount, transactionRepository, sc);
                    break;
                case 6:
                    showTotalTransactionDetails();
                    break;
                case 7:
                    System.out.println("Logged out");
                    loggedIn = false;
            }
        }
    }
}