package com.dubbo.core.example;

import java.util.concurrent.*;

public class ThreadExample {

    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        threadA.start();
        Thread thread = new Thread(threadA);
        thread.start();

        ThreadB threadB = new ThreadB();
        Thread thread1 = new Thread(threadB);
        thread1.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await(3000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("executorService run");
            }
        });
        executorService.shutdown();


        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("executor run");
                countDownLatch.countDown();
            }
        });

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("success");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("1");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("2");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("3");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("4");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("线程终止");
                        break;
                    }
                    System.out.println("i=" + (i + 1));
                }
            }
        });
        thread2.start();
        try {
            Thread.sleep(2000);
            thread2.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Object o = new Object();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        Thread.sleep(2000);
                        System.out.println();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        executor.shutdown();
    }

    static class ThreadA extends Thread {
        @Override
        public void run() {
            System.out.println("ThreadA run");
        }
    }

    static class ThreadB implements Runnable {

        @Override
        public void run() {
            System.out.println("ThreadB run");
        }
    }
}
