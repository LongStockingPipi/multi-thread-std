package pers.jason.std.multithread.jvm.jmm;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jason
 * @date 2021/8/29 22:10
 * @description 重排序示例
 *
 * results:
 * 1,1,0,1
 * 1,1,1,0
 * 1,1,0,0：发生了重排序，因为无论哪个线程，第一步都是先赋值（1），因此，理论上a、b不会出现全是0，说明在线程内部执行顺序发生变化
 * 1,1,1,1
 */
public class OutOfOrderExecution {

  static int x = 0;
  static int y = 0;
  static int a = 0;
  static int b = 0;

  public static void main(String[] args) throws InterruptedException {

    Set<String> results = new HashSet<>();

    for (;results.size() < 4;) {
      CountDownLatch countDownLatch = new CountDownLatch(1);

      Thread thread1 = new Thread(() -> {
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        x = 1;
        a = y;
      });

      Thread thread2 = new Thread(() -> {
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        y = 1;
        b = x;
      });

      thread1.start();
      thread2.start();
      countDownLatch.countDown();
      thread1.join();
      thread2.join();
      results.add(x+","+y+","+a+","+b);
      reset();
    }

    for(String s : results) {
      System.out.println(s);
    }

  }

  private static void reset() {
    x = 0;
    y = 0;
    a = 0;
    b = 0;
  }

}
