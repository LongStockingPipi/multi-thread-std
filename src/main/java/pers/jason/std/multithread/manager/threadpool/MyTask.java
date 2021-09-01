package pers.jason.std.multithread.manager.threadpool;

/**
 * @author Jason
 * @date 2021/9/1 21:38
 * @description
 */
public class MyTask implements Runnable {



  @Override
  public void run() {
    String name = Thread.currentThread().getName();
    System.out.println(name + " is running ...");
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(name + " has finished ...");
  }
}
