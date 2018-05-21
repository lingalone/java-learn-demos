package io.github.lingalone.default_interface;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/4/26
 */

public class Demo {

    public static void main(String[] args) {

        DemoInterface demoInterface = new DemoInterface() {
            @Override
            public double calculate(int a) {
                return sqrt(a);
            }
        };

        System.out.println(demoInterface.calculate(10));

    }

}
