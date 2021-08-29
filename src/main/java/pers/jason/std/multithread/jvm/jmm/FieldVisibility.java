package pers.jason.std.multithread.jvm.jmm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jason
 * @date 2021/8/29 22:46
 * @description 可见性示例
 * result:
 * a:3, b:3
 * a:3, b:2
 * a:1, b:3：发生了可见性问题，b的内容被写入主内存，但是a的内容没有写入主内存，依旧在自己线程的工作内存
 * a:1, b:2
 */
public class FieldVisibility {

  int a = 1;
  int b = 2;

  public void change() {
    a = 3;
    b = a;
  }

  public void print(Collection<String> results) {
    results.add("a:"+a+", b:"+b);
  }

  public static void main(String[] args) throws InterruptedException {

    Set<String> results = new HashSet<>();

    for(;results.size() < 4;) {
      FieldVisibility fieldVisibility = new FieldVisibility();

      Thread thread1 = new Thread(() -> {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        fieldVisibility.change();
      });


      Thread thread2 = new Thread(() -> {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        fieldVisibility.print(results);
      });

      thread1.start();
      thread2.start();

      thread1.join();
      thread2.join();
    }
    for(String s : results) {
      System.out.println(s);
    }
  }


}
