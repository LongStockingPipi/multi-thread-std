package pers.jason.std.multithread.practice.prodAndCons.wait_notify;

/**
 * @author Jason
 * @date 2021/8/17 22:45
 * @description
 */
public class CaseClass {


  /**
   * 该情况不支持 1生产者-n消费者
   * @param args
   */
  public static void main(String[] args) {
    Box box = new Box();
    Thread producer = new Thread(new CoffeeFactory(box));
    Thread consumer = new Thread(new CoffeeShop(box, "Luckin"));
    producer.start();
    consumer.start();
  }
}
