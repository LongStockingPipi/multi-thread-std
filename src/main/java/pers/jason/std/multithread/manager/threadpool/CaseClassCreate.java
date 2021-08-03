package pers.jason.std.multithread.manager.threadpool;

import java.util.concurrent.*;

/**
 * @author Jason
 * @date 2021/8/3 21:50
 * @description
 */
public class CaseClassCreate {

  /**
   * 线程池核心线程数
   */
  static final Integer CORE_POLL_SIZE = 3;

  /**
   * 线程池最大线程数
   */
  static final Integer MAXIMUM_POOL_SIZE = 5;

  /**
   * 空闲线程存活时间
   * 当线程数大于corePoolSize的时候，空闲线程一旦达到keepAliveTime，则会被销毁，直到线程数等于核心线程数corePoolSize
   * 注意：当allowCoreThreadTimeout=true的时候（默认false），则即使线程数小于核心线程数，空闲线程也会被销毁，直到线程数为0
   */
  static final Long KEEP_ALIVE_TIME = 60L;

  /**
   * 线程池使用的阻塞队列，只会保存execute()提交的Runnable任务
   * 通常有如下实现：
   *  ArrayBlockingQueue
   *  LinkedBlockingQueue
   *  SynchronousQueue
   */
  static final BlockingQueue<Runnable> DEFAULT_QUEUE = new SynchronousQueue<>();

  /**
   * 线程创建的工厂类
   */
  static final ThreadFactory DEFAULT_THREAD_FACTORY = Executors.defaultThreadFactory();

  /**
   * 任务无法提交的拒绝策略，有四种：
   *  ThreadPoolExecutor.AbortPolicy：丢弃任务 + 抛异常
   *  ThreadPoolExecutor.DiscardPolicy：丢弃任务
   *  ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，重新尝试执行任务
   *  ThreadPoolExecutor.CallerRunsPolicy：由调用线程处处理任务
   */
  static final RejectedExecutionHandler DEFAULT_REJECT_HANDLER = new ThreadPoolExecutor.AbortPolicy();

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    ExecutorService executorService1 = createThreadPool1(CORE_POLL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, DEFAULT_QUEUE);
    ExecutorService executorService2 = createThreadPool2(CORE_POLL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, DEFAULT_QUEUE, DEFAULT_THREAD_FACTORY);
    ExecutorService executorService3 = createThreadPool3(CORE_POLL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, DEFAULT_QUEUE, DEFAULT_REJECT_HANDLER);
    ExecutorService executorService4 = createThreadPool4(CORE_POLL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, DEFAULT_QUEUE, DEFAULT_THREAD_FACTORY, DEFAULT_REJECT_HANDLER);
  }

  private static ExecutorService createThreadPool1(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  private static ExecutorService createThreadPool2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  private static ExecutorService createThreadPool3(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
    return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  private static ExecutorService createThreadPool4(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }


}
