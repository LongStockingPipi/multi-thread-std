package pers.jason.std.multithread.practice.prodAndCons.wait_notify;

import pers.jason.std.multithread.practice.prodAndCons.Producer;

/**
 * @author Jason
 * @date 2021/8/17 22:36
 * @description
 */
public class CoffeeFactory implements Producer, Runnable {

  private Box box;

  public CoffeeFactory(Box box) {
    this.box = box;
  }

  @Override
  public void run() {
    for (int i = 0; i < 20; i++) {
      box.put(new Coffee());
    }
  }
}
