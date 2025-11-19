package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Email;
import com.luxoft.bankapp.domain.Queue;

public class EmailService {

    private final Queue queue = new Queue();
    private final Thread worker;
    private volatile boolean running = true;

    public EmailService() {
        worker = new Thread(() -> {
            while (running) {
                try {
                    Email email = queue.get();
                    if (email == null) {
                        break;
                    }
                    System.out.println("Sending email to " + email.getTo() + " for client "
                            + (email.getClient() != null ? email.getClient().getName() : null)
                            + " with subject: " + email.getSubject());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        worker.start();
    }

    public void sendNotificationEmail(Email email) {
        queue.add(email);
    }

    public void close() {
        running = false;
        queue.close();
        worker.interrupt();
    }
}
