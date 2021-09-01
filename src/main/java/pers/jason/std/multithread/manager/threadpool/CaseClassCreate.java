package pers.jason.std.multithread.manager.threadpool;

import java.util.concurrent.*;

/**
 * @author Jason
 * @date 2021/8/3 21:50
 * @description
 *
 * 线程池创建线程规则：
 * 1. 如果当前线程池线程数量小于核心线程数，则新建线程
 * 2. 如果当前线程池线程数量大于等于核心线程数，则将任务放到队列中
 * 3. 如果队列已经满了，并且线程池线程数量小于最大线程数，则创建线程
 * 4. 如果队列满了，并且线程池线程数达到最大线程数，则拒绝该任务的提交
 *
 * 特点：
 * 1. 如果核心线程数=最大线程数，则说明线程池大小固定
 * 2. 如果最大线程数设置为Integer.MAX_VALUE或者队列使用无界队列（LinkedBlockingQueue），则线程池大小无限
 *
 *
 * 线程池的创建：
 * 核心线程数量：
 *    如果是计算密集型的任务（加密、hash等），应该是cpu核心数量的1~2倍
 *    耗时IO型（读写文件、数据库等），应该是cpu核心数量的数倍
 * 核心线程数量=cpu核心数*(1+平均等待时间/平均工作时间)
 *
 *
 * 线程池的停止：
 * 1. shutdown()：会将存量的任务执行完毕，新任务拒绝提交
 * 2. shutDownNow()：立即停止所有任务，正在执行的任务会被中断，未执行的任务作为返回值返回
 * 3. isShutDown()：检测是否处于停止状态
 * 4. isTerminated()：线程池是否完全停止（没有任务执行）
 * 5. awaitTermination(int time, int TimeUnit)：等待一段时间，如果线程池处于完全停止状态，返回true，否则返回false
 *      该方法是阻塞的，有三种情况返回：等待时间到了、所有任务执行完毕了、等待的时候被中断了
 *
 *
 * 任务拒绝：
 * 拒绝任务的时机：
 *  1. 线程池被关闭后，再提交任务会被拒绝
 *  2. 线程池当前线程数已经达到最大线程数了，此时提交任务会被拒绝
 * 拒绝的方式：
 *  详见DEFAULT_REJECT_HANDLER参数说明
 *
 *
 * 线程池的组成：
 *  1. 线程池管理器
 *  2. 工作线程
 *  3. 任务队列
 *  4. 任务接口Task
 * 接口关系：
 *  <Executor> <- <ExecutorService> <--- [AbstractExecutorService] <- [ThreadPoolExecutor]
 *  Executor: execute(Runnable r)
 *  ExecutorService：shutdown()等方法
 *  ExecutorService：工具类
 * 线程池内部使用while循环检测新任务，然后直接调用任务的run()方法而非直接包装任务作为线程调用
 *
 * 线程池的状态：
 * RUNNING：正在工作
 * SHUTDOWN：已经关闭，不接受新任务，但依旧执行现有任务
 * STOP：停止，不接受新任务，也不处理现有任务，中断正在执行的任务
 * TIDYING：所有的任务都已经终止，并将运行terminate()钩子方法
 * TERMINATED：terminate()执行完成
 *
 */
public class CaseClassCreate {

  /**
   * 线程池核心线程数
   * 线程在完成初始化的时候是没有线程的，线程池会等待任务到来再创建线程。
   */
  static final Integer CORE_POLL_SIZE = 3;

  /**
   * 线程池最大线程数
   * 当线程池线程数达到核心线程数后，也可能会增加一些线程，但是新增加的线程数量有一个上限，即为线程池最大线程数。
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
   *  ArrayBlockingQueue：有界队列
   *  LinkedBlockingQueue：无界队列，如果任务执行速度远大于任务提交速度，则当任务过多的时候会占用大量内存，可能造成OOM
   *  SynchronousQueue：直接交接，相当于队列大小为0，此时应该将maxpoolsize设置的大一些
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
   *  ThreadPoolExecutor.DiscardOldestPolicy：丢弃最老的任务
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
