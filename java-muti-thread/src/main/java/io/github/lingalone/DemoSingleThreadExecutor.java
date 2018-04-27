package io.github.lingalone;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link https://www.jianshu.com/p/8826a459471f
 * @since 2018/4/26
 */

public class DemoSingleThreadExecutor {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        for(int i=1; i<=10; i++){
            executor.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("executor " + Thread.currentThread().getId() + " is running !");
            });

            System.out.println("thread " + i);
        }
        executor.shutdown();

    }
}
