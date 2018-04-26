package io.github.lingalone;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/4/26
 */

public class DemoCachedThreadPool {

    public static void main(String[] args) {

        Executor executor = Executors.newCachedThreadPool();
        for(int index=1;index<=5;index++){
            executor.execute(()->{
                System.out.println(Thread.currentThread().getId() +" is running !");
            });
            System.out.println("thread "+index);
        }
    }
}

//eg:
/*
thread 1
11 is running !
thread 2
thread 3
12 is running !
13 is running !
11 is running !
thread 4
thread 5
11 is running !
*/
