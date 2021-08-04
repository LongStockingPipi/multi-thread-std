package pers.jason.std.multithread.basic.lifecycle;

/**
 * @author Jason
 * @date 2021/8/3 21:16
 * @description
 */
public class CaseClassStop {

  /**
   * 正确停止线程：使用interrupt通知，而不是强制停止
   *
   * Java没有提供任何机制来安全地终止线程。最好的停止线程的方式是使用中断interrupt，中断( Interruption)是一种协作机制
   * ，但是这仅仅是会通知到被终止的线程“你该停止运行了”，被终止的线程自身拥有决定权（决定是否、以及何时停止）
   * ，这依赖于请求停止方和被停止方都遵守一种约定好的编码规范。
   *
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   *
   * 1. 线程在什么情况下会停止？
   *    在run()方法都执行完毕时；
   *    出现异常并且没有被捕获；
   *  线程停止后其占用的资源都会被JVM回收
   *
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   *
   *
   *  对于中断信号的处理：
   *  1. 优先传递异常，在方法签名处声明中断异常，让run()方法强制try-catch处理；
   *  2. 对于不能或不想传递中断的情况，可以选择使用Thread.currentThread().interrupt()恢复中断，
   *     即在接收到中断信号后，主动设置当前子线程的中断标记位；
   *  3. 不应该屏蔽中断！
   *
   *  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   *
   *  可以响应中断的方法：
   *  Object.wait()
   *  Object.wait(long)
   *  Object.wait(long, int)
   *  Thread.sleep(long)
   *  Thread.sleep(long, int)
   *  Thread.join()
   *  Thread.join(long)
   *  Thread.join(long, int)
   *  java.util.concurrent.BlockingQueue.take()
   *  java.util.concurrent.BlockingQueue.put(E)
   *  java.util.concurrent.locks.Lock.lockInterruptibly()
   *  java.util.concurrent.countDownLatch.await()
   *  java.util.concurrent.CyclicBarrier.await()
   *  java.util.concurrent.Exchange.exchange(E)
   *  java.nio.channels.InterruptibleChannel相关方法
   *  java.nio.channels.Selector相关方法
   *
   * @param args
   */
  public static void main(String[] args) {
    BaseTask task = new Task4();
    task.runTask(task, 5000);
  }

  static abstract class BaseTask implements Runnable {
    public void runTask(BaseTask task) {
      Thread thread = new Thread(task);
      thread.start();
      thread.interrupt();
    }

    public void runTask(BaseTask task, long sleepTime) {
      Thread thread = new Thread(task);
      thread.start();
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      thread.interrupt();
    }
  }


  /**
   * 普通的线程任务，在没有阻塞的情况下，使用interrupt()中断线程，但是线程本身需要不断检测isInterrupted()状态
   */
  static class Task1 extends BaseTask {

    @Override
    public void run() {
      Thread current = Thread.currentThread();
      System.out.println(current.getName() + " Running ...");
      for(int i=0;i<=Integer.MAX_VALUE/2 && !current.isInterrupted();i++) {
        if(i%10000==0) {
          System.out.print(" "+i);
        }
        if(i%500000==0) {
          System.out.println();
        }
      }
      System.out.println();
      System.out.println(current.getName() + " Finished!");
    }
  }


  /**
   * 对于存在阻塞动作的线程，在接收到中断信号的时候会抛出InterruptedException异常，因此在编写run()方法的时候需要
   * 在catch()代码块中手动处理异常逻辑来停止线程或其它动作。
   */
  static class Task2 extends BaseTask {

    @Override
    public void run() {
      Thread current = Thread.currentThread();
      try {
        System.out.println(current.getName() + " Running ...");
        for(int i=0;i<=100000 && !current.isInterrupted();i++) {
          if(i%1000==0) {
            System.out.print(" "+i);
          }
          if(i%10000==0) {
            System.out.println();
          }
        }
        Thread.sleep(5000);
        System.out.println();
        System.out.println(current.getName() + " Finished!");
      } catch (InterruptedException e) {
        System.out.println();
        System.out.println(current.getName() + " received interrupt signal!");
      }

    }
  }

  /**
   * 对于每次迭代都包含中断动作，我们的处理方式依然是通过捕获异常来停止线程，但是在迭代过程中
   * 不需要判断中断标志位current.isInterrupted()
   */
  static class Task3 extends BaseTask {

    @Override
    public void run() {
      Thread current = Thread.currentThread();
      try {
        System.out.println(current.getName() + " Running ...");
        for(int i=0;i<=10;i++) {
          System.out.print(" "+i);
          Thread.sleep(1000);
        }
        System.out.println();
        System.out.println(current.getName() + " Finished!");
      } catch (InterruptedException e) {
        System.out.println();
        System.out.println(current.getName() + " received interrupt signal!");
      }

    }
  }

  /**
   * 对于每次迭代都包含中断动作，如果try-catch放在了迭代内部，则需要手动break，否则迭代依旧继续，因为sleep()方法在响应
   * 中断信号后，会自动将中断标记位还原。因此即使每次迭代都判断!current.isInterrupted()，子线程依旧会继续工作。
   */
  static class Task4 extends BaseTask {

    @Override
    public void run() {
      Thread current = Thread.currentThread();
      System.out.println(current.getName() + " Running ...");
      for(int i=0;i<=10 && !current.isInterrupted();i++) {
        System.out.print(" "+i);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          System.out.println();
          System.out.println(current.getName() + " received interrupt signal!");
        }
      }
      System.out.println();
      System.out.println(current.getName() + " Finished!");

    }
  }
}
