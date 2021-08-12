package pers.jason.std.multithread.basic.lifecycle;

/**
 * @author Jason
 * @date 2021/8/12 21:53
 * @description
 */
public class CaseClassLifeCycle {

  /**
   * 线程一共有六种状态
   * NEW：
   *  已经创建但是未启动的新线程（未执行start()方法）
   * RUNNABLE：可运行状态
   *  一旦调用了start()方法，线程立刻计入RUNNABLE状态
   * BLOCKED：
   *  当线程要进入synchronize代码块但有未抢到锁儿等待的状态
   * WAITING：
   *  Object.wait()、Thread.join()、LockSupport.park()
   *  恢复：Object.notify()、Object.notifyAll()、LockSupport.unPark()
   * TIMED_WAITING：
   *  Object.wait(time)、Thread.join(time)、LockSupport.park(time)、LockSupport.parkNanos(time)、LockSupport.parkUtil(time)
   *  恢复：超时恢复、Object.notify()、Object.notifyAll()、LockSupport.unPark()
   * TERMINATED：
   *  run()方法执行完成
   *
   *
   *  阻塞状态：
   *    Blocked、Waiting、TimedWaiting均成为阻塞状态；
   */

  public static void main(String[] args) {
    Thread thread1 = new Thread(new Task1());
    System.out.println("状态：" + thread1.getState());
    thread1.start();
    System.out.println("状态：" + thread1.getState());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("状态：" + thread1.getState());

    System.out.println("");
    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
    System.out.println("");


    Runnable runnable = new Task2();
    Thread thread2 = new Thread(runnable);
    Thread thread3 = new Thread(runnable);
    thread2.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("thread2状态：" + thread2.getState());
    thread3.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Thread3状态：" + thread3.getState());


    System.out.println("");
    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
    System.out.println("");

    Thread thread4 = new Thread(new Task3());
    thread4.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("thread4状态：" + thread4.getState());
  }

  static class Task1 implements Runnable {
    @Override
    public void run() {
      Thread current = Thread.currentThread();
      System.out.println("  " + current.getName() + " 开始运行");
      System.out.println("状态：" + current.getState());
      for(int i=0;i<10000;i++) {}
      System.out.println("  " + current.getName() + " 运行结束");
    }
  }

  static class Task2 implements Runnable {

    @Override
    public void run() {
      System.out.println("  " + Thread.currentThread().getName() + "开始运行");
      work();
      System.out.println("  " + Thread.currentThread().getName() + "结束运行");
    }

    private synchronized void work() {
      System.out.println("    " + Thread.currentThread().getName() + "开始工作");
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        System.out.println("    " + Thread.currentThread().getName() + "结束工作");
      }
    }
  }

  static class Task3 implements Runnable {

    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + "开始运行");
      work();
      System.out.println(Thread.currentThread().getName() + "结束运行");
    }

    private synchronized void work() {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}