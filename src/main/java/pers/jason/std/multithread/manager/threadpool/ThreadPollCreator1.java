package pers.jason.std.multithread.manager.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jason
 * @date 2021/9/1 21:37
 * @description
 */
public class ThreadPollCreator1 {

  /**
   * Executors.newFixedThreadPool(int corePoolSize)：
   * 创建大小固定的线程池，使用的是无界队列 LinkedBlockingQueue
   * @param args
   */
  public static void main(String[] args) {
    ExecutorService executorService =  Executors.newFixedThreadPool(5);
    for (int i = 0; i < 100; i++) {
      executorService.submit(new MyTask());
    }
    executorService.shutdown();
  }
}
