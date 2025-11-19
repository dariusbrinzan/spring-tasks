package com.luxoft.bankapp.domain;

public class Email {

    private final Client client;
    private final String from;
    private final String to;
    private final String subject;
    private final String body;

    public Email(Client client, String from, String to, String subject, String body) {
        this.client = client;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Client getClient() {
        return client;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "client=" + (client != null ? client.getName() : null) +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
