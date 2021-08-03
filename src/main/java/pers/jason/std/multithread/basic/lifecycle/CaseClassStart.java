package pers.jason.std.multithread.basic.lifecycle;

/**
 * @author Jason
 * @date 2021/8/3 20:53
 * @description
 */
public class CaseClassStart {

  /**
    start()方法只是通知JVM有空闲的情况下启动线程，即请求JVM来运行线程，但不一定会立即运行，因此，start()执行的先后顺序与实际线程启动的顺序不一定一致；
    start()会做准备工作，使线程处于就绪状态，就绪状态表示已经获取除CPU资源以外的其它资源，如上下文、栈、PC等；
    start()方法内部执行步骤：
    1. 检查线程状态，只有在NEW状态下才会继续，否则抛出异常（因此执行两次start()会报错）
    2. 加入线程组（线程组内部使用Thread threads[]来保存）
    3. 调用start0()启动线程
   */

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " is Running"));
    thread1.start();

  }
}
