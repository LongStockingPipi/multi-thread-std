package pers.jason.std.multithread.manager.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jason
 * @date 2021/9/1 22:25
 * @description
 */
public class ThreadPoolStopper {

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executorService =  Executors.newSingleThreadExecutor();
    for (int i = 0; i < 100; i++) {
      executorService.submit(new MyTask());
    }
    Thread.sleep(1000);

    List<Runnable> unFinishedTasks = executorService.shutdownNow();
    System.out.println("未完成的任务数量：" + unFinishedTasks.size());
  }

}
