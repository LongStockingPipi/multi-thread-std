package pers.jason.std.multithread.basic.synchronize;

/**
 * @author Jason
 * @date 2021/8/25 22:58
 * @description
 */
public class App {

  Object object = new Object();

  private void work() {
    synchronized (object) {
      System.out.println("Hello World");
    }
  }

}


/*

{
  public pers.jason.std.multithread.basic.synchronize.App();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 8: 0
}




 */
