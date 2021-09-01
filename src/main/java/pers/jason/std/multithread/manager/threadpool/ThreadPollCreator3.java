package pers.jason.std.multithread.manager.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jason
 * @date 2021/9/1 21:37
 * @description
 */
public class ThreadPollCreator3 {

  /**
   * Executors.newCachedThreadPool()：
   * 使用的是 SynchronousQueue，即直接交换队列，队列大小为0
   * 线程池核心数量为0，最大数量为Integer.MAX_VALUE
   * 自动回收空闲时间达到60s的线程
   * @param args
   */
  public static void main(String[] args) {
    ExecutorService executorService =  Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      executorService.submit(new MyTask());
    }
    executorService.shutdown();
  }
}
