package com.ianpswift.utilities;

public class ThreadPool {
    public static final int THREAD_COUNT = 10;
    private Thread[] threads;
    private Thread threadPoolManager;
    private Lock lock;

    public ThreadPool() {
        threads = new Thread[THREAD_COUNT];
        lock = new Lock();

        threadPoolManager = new Thread(() -> {
            while(true) {
                for (int i = 0; i < THREAD_COUNT; i++) {
                    if (threads[i] != null && !threads[i].isAlive()) {
                        threads[i] = null;
                    }
                }
                try {
                    Thread.sleep(0,500);
                } catch (Exception e) {
                }
            }
        });

        threadPoolManager.start();
    }

    synchronized public Thread getThread(Runnable runnable) throws InterruptedException {
        lock.lock();

        int threadAcquired = -1;
        while (threadAcquired == -1) {
            threadAcquired = attemptAcquireThread(runnable);
            Thread.sleep(0,500);
        }

        lock.unlock();
        return threads[threadAcquired];
    }

    private void releaseThread(Thread thread) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (threads[i] == thread) {
                threads[i] = null;
            }
        }
    }

    private int attemptAcquireThread(Runnable runnable) throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (threads[i] == null) {
                Thread thread = new Thread(runnable);
                threads[i] = thread;
                return i;
            }
        }
        return -1;
    }
}
