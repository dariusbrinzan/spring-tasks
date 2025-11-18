package com.luxoft.bankapp.domain;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class Client {

    private String name;
    private Gender gender;
    private String city;
    private final Set<Account> accounts = new HashSet<>();

    public Client(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    public void addAccount(final Account account) {
        accounts.add(account);
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Account> getAccounts() {
        return Collections.unmodifiableSet(accounts);
    }

    public String getClientGreeting() {
        if (gender != null) {
            return gender.getGreeting() + " " + name;
        } else {
            return name;
        }
    }

    @Override
    public String toString() {
        return getClientGreeting();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return name != null && name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
}
