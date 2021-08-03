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
   * 1. 线程在什么情况下会停止？
   *    在run()方法都执行完毕时；
   *    出现异常并且没有被捕获；
   *  线程停止后其占用的资源都会被JVM回收
   * @param args
   */
  public static void main(String[] args) {
    Task1 task1 = new Task1();
    task1.runTask(task1);
  }


  /**
   * 普通的线程任务，在没有阻塞的情况下，使用interrupt()中断线程，但是线程本身需要不断检测isInterrupted()状态
   */
  static class Task1 implements Runnable {

    public void runTask(Task1 task) {
      Thread task1 = new Thread(task);
      task1.start();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      task1.interrupt();
    }

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
}
