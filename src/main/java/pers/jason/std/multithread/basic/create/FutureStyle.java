package pers.jason.std.multithread.basic.create;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Jason
 * @date 2021/9/2 20:40
 * @description
 *
 *
 * Callable 与 Future 的关系：
 * 1. 可以调用Future.get()方法获取Callable的返回值
 * 2. 可以调用Future.isDone()来判断任务是否已经结束
 * 3. 取消任务、限时获取任务执行结果等
 *
 * 主线程在调用Future.get()后，如果子线程没有执行完，则主线程进入阻塞状态
 *
 * 因此，Future是一个存储器，存储Callable的执行结果
 *
 *
 *
 * Future常用方法：
 * get()、get(long, TimeUnit)：
 *    1. 任务已经完成，则直接返回任务执行结果
 *    2. 任务尚未完成（未开始或进行中），则阻塞
 *    3. 任务执行中抛出异常，则调用get()方法的时候也会抛出异常，且get()抛出的异常永远是ExecutionException
 *    4. 任务被取消了，则get()抛出CancellationException
 *    5. 任务超时，get()重载方法支持设置超时时间，如果超时抛出TimoutException
 * cancel(boolean)：
 *    用于取消任务;
 *    参数的作用是如果该任务已经开始执行，是否用中断打断它
 * isDone()：
 *    判断线程是否执行完毕，即使执行失败了，也会返回true。
 * isCancelled()：
 *    任务是否被取消了。
 *
 *
 * FutureTask
 * 本质与Runnable、Callable一样是一种任务，可以用FutureTask来获取Future
 *                                  <--- <Runnable>
 * [FutureTask] <- <RunnableFuture>
 *                                  <--- <Future>
 * 即FutureTask即可以作为Runnable创建子线程执行任务，又可以作为Future获取执行结果
 * 开发步骤：将Callable实现作为参数生成FutureTask对象，然后将FutureTask对象作为一个Runnable实例用线程池或新建线程去执行
 *        ，最后自身调用Future接口的方法get()获取执行结果
 *
 */
public class FutureStyle {

  public static void main(String[] args) {
    ExecutorService executorService = new ThreadPoolExecutor(5, 10, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), Executors.defaultThreadFactory());
    Future<Integer> future1 = executorService.submit(new CallableTask1());
    System.out.println("执行结果：" + future1.isDone());
    try {
      System.out.println(future1.get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } finally {
      System.out.println("执行结果：" + future1.isDone());
    }

    System.out.println(" - - - - - - - - - - - - - - - - - ");

    Future<Integer> future2 = executorService.submit(new CallableTask2());
    try {
      Thread.sleep(3000);
      future2.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    System.out.println(" - - - - - - - - - - - - - - - - - ");

    Future<Integer> future31 = executorService.submit(new CallableTask3());
    try {
      future31.get(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.out.println("中断");
    } catch (ExecutionException e) {
      System.out.println("任务执行出错");
    } catch (TimeoutException e) {
      System.out.println("任务执行超时");
      future31.cancel(true);
    }

    System.out.println(" - - - - - - - - - - - - - - - - - ");

    Future<Integer> future32 = executorService.submit(new CallableTask3());
    try {
      future32.get(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.out.println("中断");
    } catch (ExecutionException e) {
      System.out.println("任务执行出错");
    } catch (TimeoutException e) {
      System.out.println("任务执行超时");
    }

    executorService.shutdown();

    System.out.println(" - - - - - - - - - - - - - - - - - ");
    FutureTask<Integer> futureTask = new FutureTask<>(new CallableTask1());
    new Thread(futureTask).start();
    try {
      System.out.println(futureTask.get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }


  }

}

class CallableTask1 implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    Thread.sleep(3000);
    return new Random().nextInt();
  }
}

class CallableTask2 implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    throw new NullPointerException("任务执行失败");
  }
}


class CallableTask3 implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    Thread.sleep(10000);
    System.out.println("CallableTask3 执行完成");
    return 0;
  }
}
