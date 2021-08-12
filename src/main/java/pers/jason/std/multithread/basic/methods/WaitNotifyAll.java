package pers.jason.std.multithread.basic.methods;

/**
 * @author Jason
 * @date 2021/8/12 23:17
 * @description
 */
public class WaitNotifyAll {

  private static Object lock = new Object();

  public static void main(String[] args) {

    Thread thread1 = new Thread(new Task());
    Thread thread2 = new Thread(new Task());

    thread1.start();
    thread2.start();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    synchronized (lock) {
      lock.notifyAll();
    }

  }

  static class Task implements Runnable {
    @Override
    public void run() {
      synchronized (lock) {
        System.out.println(Thread.currentThread().getName()+"进入同步代码块");
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"开始工作");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"退出同步代码块");
      }
    }
  }

}
