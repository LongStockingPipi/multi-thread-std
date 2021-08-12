package pers.jason.std.multithread.basic.methods;

/**
 * @author Jason
 * @date 2021/8/12 22:53
 * @description
 */
public class WaitAndNotify {

  private static Object lock = new Object();

  /**
   *  wait()、notify()、notifyAll()都需要在synchronized代码块种执行
   *  因此，有如下现象：进入synchronized代码块即获得了锁、一旦调用wait()，会释放掉锁
   *
   *
   *  wait()：让线程进入阻塞阶段，但是调用这个方法必须要持有这个对象的monitor锁
   *  遇到如下四种情况会被唤醒：
   *    1. 另一个线程调用这个对象的notify()，且正好唤醒的是该线程；
   *    2. 另一个线程调用这个对象的notifyAll()；
   *    3. 若是wait(timeout)，则在等待超时后自动唤醒（注意：timeout=0为永久等待）；
   *    4. 线程自身调用了interrupt()；
   *  notify()：会唤醒单个线程，若有多个线程都在等待，只会随机选取一个；
   *  notifyAll()：将所有等待的线程都唤醒；
   * @param args
   */
  public static void main(String[] args) {
    Thread thread1 = new Thread(new Task1());
    Thread thread2 = new Thread(new Task2());

    thread1.start();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    thread2.start();
  }

  static class Task1 implements Runnable {
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


  static class Task2 implements Runnable {
    @Override
    public void run() {
      synchronized (lock) {
        System.out.println(Thread.currentThread().getName()+"进入同步代码块");
        lock.notify();
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


