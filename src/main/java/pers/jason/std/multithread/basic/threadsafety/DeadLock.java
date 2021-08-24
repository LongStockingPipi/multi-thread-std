package pers.jason.std.multithread.basic.threadsafety;

/**
 * @author Jason
 * @date 2021/8/24 22:20
 * @description
 */
public class DeadLock implements Runnable {

  boolean flag;

  static Object lockA = new Object();

  static Object lockB = new Object();

  public DeadLock(boolean flag) {
    this.flag = flag;
  }

  @Override
  public void run() {
    String name = Thread.currentThread().getName();
    if(flag) {
      synchronized (lockA) {
        System.out.println(name + "拿到了A锁，申请B锁");
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (lockB) {
          System.out.println(name + "拿到了B锁");
        }
      }
    } else {
      synchronized (lockB) {
        System.out.println(name + "拿到了B锁，申请A锁");
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (lockA) {
          System.out.println(name + "拿到了A锁");
        }
      }
    }
  }


  public static void main(String[] args) {
    Thread thread1 = new Thread(new DeadLock(true), "线程1");
    Thread thread2 = new Thread(new DeadLock(false), "线程2");

    thread1.start();
    thread2.start();
  }
}
