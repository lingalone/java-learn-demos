package io.github.lingalone;

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

public class DemoFixedThreadPool {


    public static void main(String[] args) {

        demo_1();
    }

    /*
    thread id 1
    executor 11 is running
    thread id 2
    executor 12 is running
    thread id 3
    executor 13 is running
    executor 13 is running
    thread id 4
    thread id 5
    executor 12 is running
    executor 11 is running
    thread id 6
    thread id 7
    executor 13 is running
    executor 12 is running
    thread id 8
    thread id 9
    executor 11 is running
    thread id 10
    executor 13 is running
    */
    static void demo_1(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);


        for(int i=1;i<=10;i++){
            executorService.execute(()->{

                System.out.println("executor " + Thread.currentThread().getId() +" is running");
            });
            System.out.println("thread id "+ i);
        }
    }
}
