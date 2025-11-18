package com.luxoft.bankapp.domain;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class BankReport {

    public int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    public int getNumberOfAccounts(Bank bank) {
        int count = 0;
        for (Client c : bank.getClients()) {
            count += c.getAccounts().size();
        }
        return count;
    }

    public SortedSet<Client> getClientsSorted(Bank bank) {
        SortedSet<Client> sorted = new TreeSet<>(Comparator.comparing(Client::getName));
        sorted.addAll(bank.getClients());
        return Collections.unmodifiableSortedSet(sorted);
    }

    public double getTotalSumInAccounts(Bank bank) {
        double sum = 0;
        for (Client c : bank.getClients()) {
            for (Account a : c.getAccounts()) {
                sum += a.getBalance();
            }
        }
        return sum;
    }

    public SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        SortedSet<Account> sorted = new TreeSet<>(Comparator.comparingDouble(Account::getBalance));
        for (Client c : bank.getClients()) {
            sorted.addAll(c.getAccounts());
        }
        return Collections.unmodifiableSortedSet(sorted);
    }

    public double getBankCreditSum(Bank bank) {
        double credit = 0;
        for (Client c : bank.getClients()) {
            for (Account a : c.getAccounts()) {
                if (a instanceof CheckingAccount) {
                    CheckingAccount ca = (CheckingAccount) a;
                    if (ca.getBalance() < 0) {
                        credit += Math.abs(ca.getBalance());
                    }
                }
            }
        }
        return credit;
    }

    public Map<Client, Set<Account>> getCustomerAccounts(Bank bank) {
        Map<Client, Set<Account>> map = new HashMap<>();
        for (Client c : bank.getClients()) {
            map.put(c, c.getAccounts());
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<String, List<Client>> getClientsByCity(Bank bank) {
        Map<String, List<Client>> map = new HashMap<>();
        for (Client c : bank.getClients()) {
            String city = c.getCity();
            if (city == null) continue;
            map.computeIfAbsent(city, k -> new ArrayList<>()).add(c);
        }
        SortedSet<String> sortedKeys = new TreeSet<>(map.keySet());
        Map<String, List<Client>> sortedMap = new HashMap<>();
        for (String key : sortedKeys) {
            sortedMap.put(key, Collections.unmodifiableList(map.get(key)));
        }
        return Collections.unmodifiableMap(sortedMap);
    }
}
