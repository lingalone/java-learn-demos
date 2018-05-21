package io.github.lingalone.default_interface;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link https://github.com/winterbe/java8-tutorial#default-methods-for-interfaces
 * @since 2018/4/26
 */

public interface DemoInterface {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}

