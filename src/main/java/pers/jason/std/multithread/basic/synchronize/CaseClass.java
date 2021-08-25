package pers.jason.std.multithread.basic.synchronize;

/**
 * @author Jason
 * @date 2021/8/25 21:37
 * @description
 */
public class CaseClass {

  /**
   *
   * synchronized是Java的关键字，是最基本的互斥同步手段
   *
   * synchronized作用：
   * 保证同一时刻只有一个线程执行该段代码，以达到保证并发安全的效果。
   *
   * synchronized使用方式：
   * 1. 对象锁：
   *    1.1. 方法锁（默认使用this作为锁对象）
   *    1.2. 同步代码块锁（开发者制定锁对象）
   *
   * 2. 类锁：可以做到不同的实例之间也会互斥
   *    2.1. 修饰静态方法
   *    2.2. 指定锁为Class对象
   *    注意：对于一个Java类，可以有很多个对象实例，但是只能有一个对应的Class对象。
   *
   * synchronized特点：
   *    1. 可重入：
   *      同一个线程中，外层函数获得锁之后，内层函数可以直接再次获取该锁
   *      避免死锁，提高封装性
   *    2. 不可中断：
   *      一个线程在等待其它线程释放锁的过程中，除非另一个线程主动释放锁，否则将中断状态切永远等待下去
   *      而Lock锁则可以：
   *        等待过程中主动中断持有锁的线程
   *        等待超时退出等待
   *
   * synchronized原理：
   *    1. 每一个对象都有一个对象头，对象头会存储锁信息
   *    2. 每一个对象都与一个Monitor关联，Monitor的锁只能在同一时间被一个线程获得，获取锁、释放锁对应的指令是：Monitorenter和Monitorexit
   *      在请求Monitor锁的时候：
   *        如果Monitor计数器为0，则获得锁，计数器+1
   *        如果已经获得Monitor锁，即重入，则计数器+1
   *        如果Monitor被其它线程持有，则阻塞，直到计数器为0
   *      释放锁的时候，Monitor计数器-1，即对于重入现象，会发生多次-1操作，直到计数器清零，释放锁
   *    3. 可见性：在synchronized保护的代码块执行完毕后，在释放锁之前，会将锁住的对象所做的任何修改从本地内存（线程内存）写入到主内存，保证了可见性
   *
   * synchronized的缺点：
   *    1. 效率低
   *      锁的释放情况少（执行完成或发生异常）
   *      无法设置超时时间
   *      无法中断等待锁的线程
   *    2. 相比于读写锁，不够灵活：加锁、释放锁的条件单一，只能是一个对象
   *    3. 无法直到是否成功获得锁，相对于Lock锁，可以实现成功的逻辑和失败的逻辑（tryLock()）
   *
   * 注意：
   *  当执行的代码、调用的方法抛出异常，synchronized会自动释放锁（与Lock不同，Lock只要不显示地释放锁，就永远持有锁）
   *
   * @param args
   */
  public static void main(String[] args) {
    Runnable runnable1 = new SynchronizedObjLock1();
    Thread thread1 = new Thread(runnable1);
    Thread thread2 = new Thread(runnable1);

    thread1.start();
    thread2.start();


    wait(thread1, thread2);


    Runnable runnable2 = new SynchronizedObjLock2();
    Thread thread3 = new Thread(runnable2);
    Thread thread4 = new Thread(runnable2);

    thread3.start();
    thread4.start();

    wait(thread3, thread4);


    Runnable runnable3 = new SynchronizedClassLock1();
    Runnable runnable4 = new SynchronizedClassLock1();
    Thread thread5 = new Thread(runnable3);
    Thread thread6 = new Thread(runnable4);
    thread5.start();
    thread6.start();


    wait(thread5, thread6);


    Runnable runnable5 = new SynchronizedClassLock2();
    Runnable runnable6 = new SynchronizedClassLock2();
    Thread thread7 = new Thread(runnable5);
    Thread thread8 = new Thread(runnable6);
    thread7.start();
    thread8.start();

  }

  private static void wait(Thread ... threads) {
    for(Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - ");
  }

}


/**
 * 对象锁示例1：同步代码块
 */
class SynchronizedObjLock1 implements Runnable {

  static final Object lock = new Object();

  @Override
  public void run() {
    synchronized (lock) {
      System.out.println(Thread.currentThread().getName() + "获得了锁，开始运行...");
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "运行结束，释放锁。");
    }
  }
}

/**
 * 对象锁示例2：同步方法
 */
class SynchronizedObjLock2 implements Runnable {

  @Override
  public void run() {
    work();
  }

  private synchronized void work() {
    System.out.println(Thread.currentThread().getName() + "获得了锁，开始运行...");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + "运行结束，释放锁。");
  }
}


/**
 * 类锁示例1：同步静态方法
 */
class SynchronizedClassLock1 implements Runnable {

  @Override
  public void run() {
    work();
  }

  private static synchronized void work() {
    System.out.println(Thread.currentThread().getName() + "获得了锁，开始运行...");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + "运行结束，释放锁。");
  }
}


/**
 * 类锁示例2：同步代码块
 */
class SynchronizedClassLock2 implements Runnable {

  @Override
  public void run() {
    synchronized (SynchronizedClassLock2.class) {
      System.out.println(Thread.currentThread().getName() + "获得了锁，开始运行...");
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "运行结束，释放锁。");
    }
  }
}
