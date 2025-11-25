package com.luxoft.bankapp.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BankReportStreams {
    // Returns the total number of clients in the bank
    public int getNumberOfClients(Bank bank) {
        return (int) bank.getClients().stream().count();
    }
    // Counts all accounts across all clients and returns the total
    public int getNumberOfAccounts(Bank bank) {
        return bank.getClients().stream()
                .mapToInt(c -> c.getAccounts().size())
                .sum();
    }
    // Returns clients sorted alphabetically by name in an immutable SortedSet
    public SortedSet<Client> getClientsSorted(Bank bank) {
        SortedSet<Client> sorted = bank.getClients().stream()
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(Client::getName))
                ));
        return Collections.unmodifiableSortedSet(sorted);
    }
    // Calculates total balance by summing all accounts from all clients
    public double getTotalSumInAccounts(Bank bank) {
        return bank.getClients().stream()
                .flatMap(c -> c.getAccounts().stream())
                .mapToDouble(Account::getBalance)
                .sum();
    }
    // Returns all accounts sorted by balance in ascending order
    public SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        SortedSet<Account> sorted = bank.getClients().stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparingDouble(Account::getBalance))
                ));
        return Collections.unmodifiableSortedSet(sorted);
    }
    // Sums negative balances from CheckingAccount to compute total credit used
    public double getBankCreditSum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(c -> c.getAccounts().stream())
                .filter(a -> a instanceof CheckingAccount)
                .map(a -> (CheckingAccount) a)
                .mapToDouble(ca -> ca.getBalance() < 0 ? -ca.getBalance() : 0.0)
                .sum();
    }
    // Maps each client to their account collection, returned as unmodifiable
    public Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        Map<Client, Collection<Account>> map = bank.getClients().stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> c.getAccounts()
                ));
        return Collections.unmodifiableMap(map);
    }
    // Groups clients by city into a sorted, immutable map
    public Map<String, List<Client>> getClientsByCity(Bank bank) {
        Map<String, List<Client>> grouped = bank.getClients().stream()
                .filter(c -> c.getCity() != null)
                .collect(Collectors.groupingBy(
                        Client::getCity,
                        TreeMap::new,
                        Collectors.toList()
                ));

        Map<String, List<Client>> unmodifiable = grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Collections.unmodifiableList(e.getValue()),
                        (a, b) -> a,
                        TreeMap::new
                ));

        return Collections.unmodifiableMap(unmodifiable);
    }
}
