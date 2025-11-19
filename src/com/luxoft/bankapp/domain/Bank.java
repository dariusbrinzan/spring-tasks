package com.luxoft.bankapp.domain;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.EmailService;
import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class Bank {

    private final Set<Client> clients = new HashSet<>();
    private final List<ClientRegistrationListener> listeners = new ArrayList<>();
    private final EmailService emailService = new EmailService();

    private int printedClients = 0;
    private int emailedClients = 0;
    private int debuggedClients = 0;

    public Bank() {
        listeners.add(new PrintClientListener());
        listeners.add(new EmailNotificationListener());
        listeners.add(new DebugListener());
    }

    public int getPrintedClients() {
        return printedClients;
    }

    public int getEmailedClients() {
        return emailedClients;
    }

    public int getDebuggedClients() {
        return debuggedClients;
    }

    public void addClient(final Client client) throws ClientExistsException {
        boolean added = clients.add(client);
        if (!added) {
            throw new ClientExistsException("Client already exists into the bank");
        }
        notify(client);
    }

    private void notify(Client client) {
        for (ClientRegistrationListener listener : listeners) {
            listener.onClientAdded(client);
        }
    }

    public Set<Client> getClients() {
        return Collections.unmodifiableSet(clients);
    }

    public void close() {
        emailService.close();
    }

    class PrintClientListener implements ClientRegistrationListener {
        @Override
        public void onClientAdded(Client client) {
            System.out.println("Client added: " + client.getName());
            printedClients++;
        }
    }

    class EmailNotificationListener implements ClientRegistrationListener {
        @Override
        public void onClientAdded(Client client) {
            Email email = new Email(
                    client,
                    "bank@bank.com",
                    client.getName().replace(" ", "").toLowerCase() + "@mail.com",
                    "Welcome",
                    "Hello " + client.getName()
            );
            emailService.sendNotificationEmail(email);
            emailedClients++;
        }
    }

    class DebugListener implements ClientRegistrationListener {
        @Override
        public void onClientAdded(Client client) {
            System.out.println("Client " + client.getName() + " added on: "
                    + DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
            debuggedClients++;
        }
    }
}
