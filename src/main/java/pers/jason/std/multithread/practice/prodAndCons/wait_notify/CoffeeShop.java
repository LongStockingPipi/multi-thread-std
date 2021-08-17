package pers.jason.std.multithread.practice.prodAndCons.wait_notify;

import pers.jason.std.multithread.practice.prodAndCons.Consumer;

/**
 * @author Jason
 * @date 2021/8/17 22:36
 * @description
 */
public class CoffeeShop implements Consumer, Runnable {

  private Box box;

  private String shopName;

  public CoffeeShop(Box box, String shopName) {
    this.box = box;
    this.shopName = shopName;
  }

  @Override
  public void run() {
    for (int i = 0; i < 10; i++) {
      box.get(shopName);
    }
  }
}
