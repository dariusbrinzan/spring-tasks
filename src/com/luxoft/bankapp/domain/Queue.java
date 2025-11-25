package com.luxoft.bankapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.luxoft.bankapp.domain.Email;

public class Queue {

    // Holds pending Email objects to be processed
    private final List<Email> queue = new ArrayList<>();
    // Indicates whether the queue accepts new emails
    private boolean closed = false;
    // Adds email to queue and wakes waiting threads
    public synchronized void add(Email email) {
        if (closed) {
            return;
        }
        queue.add(email);
        notify();
    }
    // Blocks until an email is available or queue is closed
    public synchronized Email get() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }
        if (queue.isEmpty() && closed) {
            return null;
        }
        return queue.remove(0);
    }
    // Marks queue closed and wakes all waiting threads
    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
