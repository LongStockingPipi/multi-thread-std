package pers.jason.std.multithread.basic.threadsafety;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jason
 * @date 2021/8/23 22:00
 * @description 演示计数不准确
 *
 * 31901出错了
 * 49850出错了
 * 总计 : 200000
 * 错误 : 2
 * 结果 : 199998
 *
 *
 */
public class AddA implements Runnable {

  int index = 0;
  
  static AtomicInteger count = new AtomicInteger();

  static AtomicInteger error = new AtomicInteger();

  static CyclicBarrier startCyclicBarrier = new CyclicBarrier(2);

  static CyclicBarrier endCyclicBarrier = new CyclicBarrier(2);

  static boolean[] array = new boolean[200001];

  static {
    array[0] = true;
  }

  @Override
  public void run() {
    for (int i = 0; i < 100000; i++) {
      try {
        endCyclicBarrier.reset();
        startCyclicBarrier.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      }
      index ++;
      try {
        startCyclicBarrier.reset();
        endCyclicBarrier.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      }
      count.incrementAndGet();

      synchronized (this) {
        if(array[index] && array[index-1]) {
          System.out.println(index + "出错了");
          error.incrementAndGet();
        } else {
          array[index] = true;
        }
      }
    }
  }

  public static void main(String[] args) {
    AddA runnable = new AddA();
    Thread thread1 = new Thread(runnable);
    Thread thread2 = new Thread(runnable);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    System.out.println("总计 : " + AddA.count);
    System.out.println("错误 : " + AddA.error);
    System.out.println("结果 : " + runnable.index);
  }

}
