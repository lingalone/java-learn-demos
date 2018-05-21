package io.github.lingalone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link http://www.infoq.com/cn/articles/cf-java-reflection-dynamic-proxy
 * @since 2018/4/27
 */

public class JavaReflect {

    public static void main(String[] args) throws Throwable {

        DemoClass demoClass = new DemoClass();
        Class cls = demoClass.getClass();

        try {
            System.out.println(cls);
            Method demoMethod = cls.getDeclaredMethod("demo", String.class);
            demoMethod.invoke(demoClass,"ling-alone");


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // https://mp.weixin.qq.com/s/dbJSPCr6pdPHjVhhKMhaDQ
           throw e.getCause();
        }


    }

}

class DemoClass{
    public String demo(String demo) throws Exception{

        System.out.println("reflect test !");
        if(demo.contains("ling"))
            throw new Exception("contains ling");
        return demo + " : hello world";
    }

}