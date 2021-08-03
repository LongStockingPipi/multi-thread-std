package pers.jason.std.multithread.basic.create;

import java.util.concurrent.Callable;

/**
 * @author Jason
 * @date 2021/8/2 22:37
 * @description
 */
public class CallableStyle implements Callable<String> {

  static final String SUCCESS = "success";

  @Override
  public String call() throws Exception {
    try {
      System.out.println(Thread.currentThread().getName() + "Callable Style Running ...");
      Thread.sleep(10000);
      return SUCCESS;
    } catch (Exception e) {
      throw new Exception("Run failure !", e);
    }
  }
}
