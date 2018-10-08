package io.github.lingalone.jvm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/9/29
 */

public class ClassLoaderProccess {


    static final int f = 100;
    static int i = 100;

    static {
        System.out.println("static-f->"+f);
        System.out.println("static-i->"+i);
    }


    public ClassLoaderProccess() {
        System.out.println("construct-f->"+f);
        System.out.println("construct-i->"+i);
    }


    public static void main(String[] args) {
        new ClassLoaderProccess();
    }

}
