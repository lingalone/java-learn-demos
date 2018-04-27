package io.github.lingalone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link https://www.jianshu.com/p/8826a459471f
 * @since 2018/4/26
 */

public class DemoCachedThreadPool {

    public static void main(String[] args) {

//        demo_1();
        demo_2();
//        demo_3();
    }


    static void demo_1(){
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
        Executor executor = Executors.newCachedThreadPool();
        for(int index=1; index<=5; index++){
            executor.execute(()->{
                System.out.println(Thread.currentThread().getId() +" is running !");
            });
            System.out.println("thread "+index);
        }
    }

    static void demo_2(){
        Executor executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread newT = new Thread(r);
                System.out.println("ThreadFactory "+newT.getId() +" is running !");
                return newT;
            }
        });
        for(int index=1; index<=5; index++){
            executor.execute(()->{
                System.out.println("Executor "+Thread.currentThread().getId() +" is running !");
            });
            System.out.println("thread "+index);
        }
    }


    static void demo_3(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();

        for(int index=1; index<=10; index++){
            Future<String> future = executorService.submit(() -> {
                long sleepTime = (long) ((Math.random()*100+100)*100);
                System.out.println("Executor "+Thread.currentThread().getId()+" sleep time "+ sleepTime);
                Thread.sleep(sleepTime);
                return "Executor "+Thread.currentThread().getId() +" is running !";
            });
            System.out.println("thread "+index);

            futures.add(future);
        }

        for (Future<String> future:futures){

            try {
//                while(!future.isDone());
                try {
                    System.out.println(future.get(10, TimeUnit.SECONDS));
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }

    }

}





