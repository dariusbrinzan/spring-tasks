package com.luxoft.bankapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.luxoft.bankapp.domain.Email;

public class Queue {

    private final List<Email> queue = new ArrayList<>();
    private boolean closed = false;

    public synchronized void add(Email email) {
        if (closed) {
            return;
        }
        queue.add(email);
        notify();
    }

    public synchronized Email get() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }
        if (queue.isEmpty() && closed) {
            return null;
        }
        return queue.remove(0);
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
