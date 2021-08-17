package pers.jason.std.multithread.practice.prodAndCons.wait_notify;

import pers.jason.std.multithread.practice.prodAndCons.Container;

import java.util.LinkedList;

/**
 * @author Jason
 * @date 2021/8/17 22:37
 * @description
 */
public class Box implements Container {

  private final int maxSize = 10;

  private final LinkedList<Coffee> coffeeList = new LinkedList<>();

  public synchronized void put(Coffee coffee) {
    if(coffeeList.size() == maxSize) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    coffeeList.add(coffee);
    System.out.println("生产了一杯咖啡，当前箱子有：" + coffeeList.size() + "杯咖啡");
    notify();
  }

  public synchronized Coffee get(String shopName) {
    if(coffeeList.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Coffee coffee = coffeeList.poll();
    System.out.println("  " + shopName + "取走了一杯咖啡，当前箱子有：" + coffeeList.size() + "杯咖啡");
    notify();
    return coffee;
  }

}
