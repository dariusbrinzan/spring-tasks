package com.luxoft.bankapp.main;

import java.util.Map;
import java.util.Scanner;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.BankReport;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.domain.SavingAccount;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.service.BankService;

public class BankApplication {

    private static Bank bank;

    public static void main(String[] args) {
        bank = new Bank();
        modifyBank();

        if (args.length > 0 && "-statistics".equals(args[0])) {
            runStatisticsMode();
        } else {
            runNormalMode();
        }

        bank.close();
    }

    private static void modifyBank() {
        Client client1 = new Client("John", Gender.MALE);
        Account account1 = new SavingAccount(1, 100);
        Account account2 = new CheckingAccount(2, 100, 20);
        client1.addAccount(account1);
        client1.addAccount(account2);

        try {
            BankService.addClient(bank, client1);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client1.getName());
        }

        account1.deposit(100);
        try {
            account1.withdraw(10);
        } catch (OverdraftLimitExceededException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            account2.withdraw(90);
        } catch (OverdraftLimitExceededException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            account2.withdraw(100);
        } catch (OverdraftLimitExceededException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format(
                    "Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n",
                    e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            BankService.addClient(bank, client1);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client1);
        }
    }

    private static void runNormalMode() {
        printBalance();
        BankService.printMaximumAmountToWithdraw(bank);
    }

    private static void printBalance() {
        System.out.format("%nPrint balance for all clients%n");
        for (Client client : bank.getClients()) {
            System.out.println("Client: " + client);
            for (Account account : client.getAccounts()) {
                System.out.format("Account %d : %.2f%n", account.getId(), account.getBalance());
            }
        }
    }

    private static void runStatisticsMode() {
        BankReport report = new BankReport();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'display statistic' to show bank statistics or 'exit' to quit.");

        while (true) {
            String line = scanner.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            if ("display statistic".equalsIgnoreCase(line)) {
                displayStatistics(report);
            } else {
                System.out.println("Unknown command. Type 'display statistic' or 'exit'.");
            }
        }
    }

    private static void displayStatistics(BankReport report) {
        System.out.println("Number of clients: " + report.getNumberOfClients(bank));
        System.out.println("Number of accounts: " + report.getNumberOfAccounts(bank));
        System.out.println("Total sum in accounts: " + report.getTotalSumInAccounts(bank));
        System.out.println("Bank credit sum: " + report.getBankCreditSum(bank));

        System.out.println("Clients sorted:");
        for (Client c : report.getClientsSorted(bank)) {
            System.out.println("  " + c.getClientGreeting());
        }

        System.out.println("Accounts sorted by balance:");
        for (Account a : report.getAccountsSortedBySum(bank)) {
            System.out.println("  id=" + a.getId() + ", balance=" + a.getBalance());
        }

        System.out.println("Clients by city:");
        for (Map.Entry<String, java.util.List<Client>> entry : report.getClientsByCity(bank).entrySet()) {
            System.out.println("City: " + entry.getKey());
            for (Client c : entry.getValue()) {
                System.out.println("  " + c.getName());
            }
        }
    }
}
