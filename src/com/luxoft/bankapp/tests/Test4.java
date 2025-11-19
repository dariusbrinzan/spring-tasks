package com.luxoft.bankapp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.BankReportStreams;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.domain.SavingAccount;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankService;

public class Test4 {

    private Bank bank;
    private BankReportStreams report;
    private Client client1;
    private Client client2;
    private Client client3;

    @Before
    public void setup() throws ClientExistsException {
        bank = new Bank();
        report = new BankReportStreams();

        client1 = new Client("Smith John", Gender.MALE);
        client1.setCity("Bucharest");
        client1.addAccount(new SavingAccount(1, 1000.0));
        client1.addAccount(new CheckingAccount(2, -50.0, 100.0));

        client2 = new Client("Alice Brown", Gender.FEMALE);
        client2.setCity("Cluj");
        client2.addAccount(new SavingAccount(3, 2000.0));
        client2.addAccount(new CheckingAccount(4, -150.0, 300.0));

        client3 = new Client("Bob White", Gender.MALE);
        client3.setCity("Bucharest");
        client3.addAccount(new SavingAccount(5, 300.0));

        BankService.addClient(bank, client1);
        BankService.addClient(bank, client2);
        BankService.addClient(bank, client3);
    }

    @Test
    public void testGetNumberOfClients() {
        assertEquals(3, report.getNumberOfClients(bank));
    }

    @Test
    public void testGetNumberOfAccounts() {
        assertEquals(5, report.getNumberOfAccounts(bank));
    }

    @Test
    public void testGetClientsSorted() {
        SortedSet<Client> sortedClients = report.getClientsSorted(bank);
        Client first = sortedClients.first();
        Client last = sortedClients.last();

        assertEquals("Alice Brown", first.getName());
        assertEquals("Smith John", last.getName());
    }

    @Test
    public void testGetTotalSumInAccounts() {
        double expected = 1000.0 + (-50.0) + 2000.0 + (-150.0) + 300.0;
        assertEquals(expected, report.getTotalSumInAccounts(bank), 0.0001);
    }

    @Test
    public void testGetAccountsSortedBySum() {
        SortedSet<Account> sortedAccounts = report.getAccountsSortedBySum(bank);
        Account first = sortedAccounts.first();
        Account last = sortedAccounts.last();

        assertEquals(-150.0, first.getBalance(), 0.0001);
        assertEquals(2000.0, last.getBalance(), 0.0001);

        double prev = Double.NEGATIVE_INFINITY;
        for (Account a : sortedAccounts) {
            assertTrue(a.getBalance() >= prev);
            prev = a.getBalance();
        }
    }

    @Test
    public void testGetBankCreditSum() {
        double expectedCredit = 50.0 + 150.0;
        assertEquals(expectedCredit, report.getBankCreditSum(bank), 0.0001);
    }

    @Test
    public void testGetCustomerAccounts() {
        Map<Client, Collection<Account>> customerAccounts = report.getCustomerAccounts(bank);

        assertEquals(3, customerAccounts.size());
        assertEquals(2, customerAccounts.get(client1).size());
        assertEquals(2, customerAccounts.get(client2).size());
        assertEquals(1, customerAccounts.get(client3).size());
    }

    @Test
    public void testGetClientsByCity() {
        Map<String, List<Client>> clientsByCity = report.getClientsByCity(bank);

        assertEquals(2, clientsByCity.size());
        assertTrue(clientsByCity.containsKey("Bucharest"));
        assertTrue(clientsByCity.containsKey("Cluj"));

        List<Client> bucharestClients = clientsByCity.get("Bucharest");
        List<Client> clujClients = clientsByCity.get("Cluj");

        assertEquals(2, bucharestClients.size());
        assertEquals(1, clujClients.size());

        boolean hasJohn = bucharestClients.stream()
                .anyMatch(c -> "Smith John".equals(c.getName()));
        boolean hasBob = bucharestClients.stream()
                .anyMatch(c -> "Bob White".equals(c.getName()));

        assertTrue(hasJohn);
        assertTrue(hasBob);
        assertEquals("Alice Brown", clujClients.get(0).getName());
    }
}
