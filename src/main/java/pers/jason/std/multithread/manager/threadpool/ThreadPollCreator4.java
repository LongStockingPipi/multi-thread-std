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
public class ThreadPollCreator4 {

  /**
   * Executors.newScheduledThreadPool()：
   * 支持定时、周期性的执行任务
   * @param args
   */
  public static void main(String[] args) throws InterruptedException {
    ScheduledExecutorService executorService =  Executors.newScheduledThreadPool(5);
    /**
     * 延迟5s运行
     */
    executorService.schedule(new MyTask(), 5, TimeUnit.SECONDS);

    /**
     * 首次延迟5s，以后每隔1s执行一次
     */
    executorService.scheduleAtFixedRate(new MyTask(), 5, 1, TimeUnit.SECONDS);

    Thread.sleep(1500);
    executorService.shutdown();

  }
}
