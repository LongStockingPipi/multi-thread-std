package pers.jason.std.multithread.manager.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Jason
 * @date 2021/9/1 21:37
 * @description
 */
public class ThreadPollCreator5 {

  /**
   * Executors.newWorkStealingPool()：
   * jdk1.8加入
   * 支持子任务
   * 有窃取功能
   * 不保证执行顺序
   * @param args
   */
  public static void main(String[] args) {
    ExecutorService executorService =  Executors.newWorkStealingPool();

    for (int i = 0; i < 10; i++) {
      executorService.submit(new MyTask());
    }


  }
}
