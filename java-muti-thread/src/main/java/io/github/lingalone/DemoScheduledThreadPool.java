package io.github.lingalone;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/4/26
 */

public class DemoScheduledThreadPool {

    public static void main(String[] args) {
        demo_2();
    }

    static void demo_1(){

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3,
                (r) ->{
                    Thread newT = new Thread(r);
                    System.out.println("create thread " + newT.getId() + " time "+System.currentTimeMillis());
                    return newT;
        });

        for(int i=1; i<=10 ;i++){
            scheduledExecutorService.schedule(()->{
                System.out.println("executor "+Thread.currentThread().getId() +" is running !" + " time "+System.currentTimeMillis());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },10, TimeUnit.SECONDS);

            System.out.println("thread start" + i + " time "+System.currentTimeMillis());

        }
        scheduledExecutorService.shutdown();
    }

    static void demo_2(){

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3,
                (r) ->{
                    Thread newT = new Thread(r);
                    System.out.println("create thread " + newT.getId() + " time "+System.currentTimeMillis());
                    return newT;
                });

        for(int i=1; i<=10 ;i++){
            scheduledExecutorService.scheduleAtFixedRate(()->{
                System.out.println("executor "+Thread.currentThread().getId() +" is running !" + " time "+System.currentTimeMillis());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },10, 9,TimeUnit.SECONDS);

            System.out.println("thread start" + i + " time "+System.currentTimeMillis());

        }
//        scheduledExecutorService.shutdown();
    }
}
