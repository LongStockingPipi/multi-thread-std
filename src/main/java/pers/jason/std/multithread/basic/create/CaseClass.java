package pers.jason.std.multithread.basic.create;

import pers.jason.std.multithread.support.BaseCase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class CaseClass extends BaseCase {

  /**
   * 根据oracle官网：
   * 创建一个线程有两种方式：
   *  1. 继承Thread类
   *  2. 实现Runnable接口
   *
   * 发现：
   *  1. 对于run()方法，实现Runnable接口的方式是调用target.run()，
   *  而继承Thread类则是直接重写了run()方法。
   *  2. 对于第三种使用匿名内部类既重写run()又实现run()，以外层的重写方法为准。
   *  3. 相对于继承Thread类，实现Runnable接口有如下好处：
   *    代码架构角度做到了解耦、java单继承，增加了可扩展性；
   */

  @Override
  public void run() {

    /* 1. 继承Thread类 */
    Thread th1 = new Thread(new RunnableStyle());
    th1.start();

    /* 2. 实现Runnable接口 */
    Thread th2 = new ThreadStyle();
    th2.start();

    /* 3. 匿名内部类 */
    Thread th3 = new Thread(() -> System.out.println("From Runnable ...")) {
      @Override
      public void run() {
        System.out.println("From Thread ...");
      }
    };
    th3.start();

    /* 4. 线程池 */
    ExecutorService executorService = Executors.newCachedThreadPool();
    for(int i=0;i<5;i++) {
      executorService.submit(() -> {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finished ...");
      });
    }

    /* 5. 定时器 */
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " Finished!");
      }
    }, 1000, 1000);

    /* 6. lambda表达式 */
    new Thread(() -> System.out.println("With lambda ...")).start();

    /* 7. Callable接口 */
    Callable<String> callable = new CallableStyle();
    FutureTask<String> futureTask = new FutureTask<>(callable);
    new Thread(futureTask).start();
    try {
      System.out.println("执行结果："
//          + futureTask.get()
          + futureTask.get(5, TimeUnit.SECONDS)
      );
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      System.out.println("获取结果超时！");
      e.printStackTrace();
    }


  }

}
