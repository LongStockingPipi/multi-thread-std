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
   *
   *
   *  特点：
   *  1. 必须持有monitor；
   *  2. notify只能唤醒一个线程；
   *  3. 三个方法都属于Object类；
   *  4. jdk对于wait-notify有封装：Condition；
   *  2. 只能唤醒对象本身的锁；
   *
   *
   *  原理：
   *  monitor本身有入口集和等待集，抢夺锁的线程都会进入入口集中，而调用wait()后的线程会进入等待集中，直到被唤醒才会继续抢锁；
   *  由于调用notify()也需要持有monitor锁，因此通常被唤醒的线程不会立即得到锁，虽然状态图中WAITING->RUNNABLE，但是实际上通常是WAITING->BLOCKED
   *
   *
   *  面试：
   *  1. 为什么执行wait需要在同步代码块中
   *  如果不在同步代码块中，会出现线程直线切换执行，不能保证wait-notify执行顺序，一旦切换到notify()线程，再切换回来执行wait()，则会发生死锁；
   *  2. 为什么这三个方法是Object类中？
   *  因为这三个方法是锁级别的操作，锁本身就是一个对象Object，而像sleep等方法是线程级别的操作
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


