package pers.jason.std.multithread.practice.odd_even;

/**
 * @author Jason
 * @date 2021/8/17 22:58
 * @description 交替打印奇偶数
 */
public class CaseClass {

  static int count = 0;

  static Object lock = new Object();


  public static void main(String[] args) {
    Thread thread1 = new Thread(new Task());
    Thread thread2 = new Thread(new Task());

    thread1.start();
    thread2.start();
  }

  static class Task implements Runnable {


    @Override
    public void run() {
      while(count <= 100) {
        synchronized (lock) {
          System.out.println(Thread.currentThread().getName() + " " + count);
          count ++;
          lock.notify();
          if(count <= 100) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
