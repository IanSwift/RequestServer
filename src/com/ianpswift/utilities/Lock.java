package com.ianpswift.utilities;

public class Lock {
    private int count;

    synchronized  public void lock() throws InterruptedException {
        while (count != 0) {
            Thread.sleep(0, ((int)(Math.random()*200)) + 567);
        }
        count++;
        if (count > 1) {
            count--;
            lock();
        }
    }

    public void unlock() {
        count--;
    }
}
