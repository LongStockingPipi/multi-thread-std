package pers.jason.std.multithread.basic.methods;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jason
 * @date 2021/8/18 21:36
 * @description
 */
public class Sleep {

  /**
   * sleep
   * 作用：让线程进入WAITING状态，切不占用CPU资源，但不释放锁
   *
   * 特点：
   *  不释放锁，包括synchronized、Lock
   *  可响应中断，先抛异常，再清除中断状态
   * @param args
   */
  public static void main(String[] args) {
    Task task = new Task();
    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);

    thread1.start();
    thread2.start();

    try {
      Thread.sleep(11000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //------------------------------------------------------

    Task2 task2 = new Task2();
    Thread thread3 = new Thread(task2);
    Thread thread4 = new Thread(task2);

    thread3.start();
    thread4.start();

  }

  static class Task implements Runnable {
    @Override
    public void run() {
      work();
    }

    private synchronized void work() {
      System.out.println(Thread.currentThread().getName() + "获得monitor锁");
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "退出同步代码块");
    }
  }


  static class Task2 implements Runnable {

    private static final Lock lock = new ReentrantLock();

    @Override
    public void run() {
      lock.lock();
      System.out.println(Thread.currentThread().getName() + "获得lock");
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println(Thread.currentThread().getName() + "释放了lock");
        lock.unlock();
      }
    }
  }
}
