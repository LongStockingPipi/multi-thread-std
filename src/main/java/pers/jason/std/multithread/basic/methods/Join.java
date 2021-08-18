package pers.jason.std.multithread.basic.methods;

import java.util.concurrent.TimeUnit;

/**
 * @author Jason
 * @date 2021/8/18 22:12
 * @description
 */
public class Join {

  /**
   * join的作用
   *  新的线程加入，等待新线程执行完本线程再执行
   *
   * 用法：
   *  通常是main线程等待子线程执行完
   *
   * 中断：
   *  主线程（执行等待动作的线程）在等待期间发生中断，则不会继续等待子线程执行，而是执行本线程的代码；可以将中断传递给子线程解决
   *
   * join()等待期限，线程处于WAITING状态
   *
   * 原理：
   *  join本身内部使用wait()将自己挂起阻塞，而锁对象是子线程对象，由于Thread类作为锁的时候，run()方法执行完成就会自动调用notifyAll(),这样就可以唤醒等待的线程
   *
   * @param args
   */
  public static void main(String[] args) {
    Thread thread1 = new Thread(new JoinTask());
    Thread thread2 = new Thread(new JoinTask());

    thread1.start();
    thread2.start();
    System.out.println("开始等待子线程执行完毕");
    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("所有子线程执行完毕");


    Thread thread3 = new Thread(new JoinTask2(Thread.currentThread()));
    thread3.start();
    System.out.println("开始等待子线程执行完毕");
    try {
      thread3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("所有子线程执行完毕");

  }

  static class JoinTask implements Runnable {
    @Override
    public void run() {
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println(Thread.currentThread().getName() + "执行完毕");
      }
    }
  }

  static class JoinTask2 implements Runnable {

    final Thread main;

    public JoinTask2(Thread main) {
      this.main = main;
    }

    @Override
    public void run() {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      main.interrupt();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println(Thread.currentThread().getName() + "执行完毕");
      }
    }
  }
}
