package io.github.lingalone.thread_local;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link <a href="http://www.importnew.com/20963.html">ThreadLocal工作原理</a>
 * @since 2018/6/26
 */

public class Demo {


    private static Executor executor = Executors.newFixedThreadPool(10);


    /*
    * qa:
    * ThreadLocal是怎么实现了多个线程之间每个线程一个变量副本的？它是如何实现共享变量的。
    * */

    public static void main(String[] args) {

        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        /*
        *   1. ThreadLocal --define-->  ThreadLocalMap  <--init-- Thread
        *   2. ThreadLocal.set(val) --get--> Thread.currentThread() --get--> Thread.ThreadLocal.ThreadLocalMap threadLocals  --set--> threadLocals.set(this,val)
        *   3. ThreadLocal.get(val) --get--> Thread.currentThread() --get--> Thread.ThreadLocal.ThreadLocalMap threadLocals  --get--> threadLocals.get(this)
        *   4. if threadLocals==null --create--> Thread.ThreadLocal.ThreadLocalMap threadLocals
        *   5. importance Thread.currentThread() == this
        * */

        executor.execute(new Thread(()->{
            threadLocal.set("qqqqq");
            System.out.println("in -> "+ Thread.currentThread() +" "+threadLocal.get());
        }));

        threadLocal.set("aaaaa");
        System.out.println("out -> "+ Thread.currentThread() +" "+threadLocal.get());
    }
}
