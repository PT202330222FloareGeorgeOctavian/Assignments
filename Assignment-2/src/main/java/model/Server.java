package model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements Runnable {
    private ArrayBlockingQueue<Task> server;
    private AtomicInteger waitingTime;
    private Task currentTask;
    private boolean running = true;

    public Server() {
        server = new ArrayBlockingQueue<>(100);
        waitingTime = new AtomicInteger(0);
    }

    public void addTask(Task t) {
        this.server.add(t);
        this.waitingTime.addAndGet(t.getServiceTime().get());
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public ArrayBlockingQueue<Task> getServer() {
        return server;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public boolean isRunning() {
        return running;
    }

    public void setTaskNull() {
        this.currentTask = null;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (!server.isEmpty() || currentTask != null) {
                try {
                    if (currentTask == null || currentTask.getServiceTime().get() == 0) {
                        currentTask = server.take();
                    }
                    Thread.sleep(1000);
                    this.waitingTime.decrementAndGet();
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

    }
}

