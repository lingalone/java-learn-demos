package io.github.lingalone;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/9/13
 */

public class TestChild extends TestParent {

//    private void ttt(){};     //重写原则  X
    private void ttt(){
        System.out.println("22");
    };
    public void ttt(String ddd){
        System.out.println(ddd);
    };
//    private void ttt(String qq){
//        System.out.println();
//    };
//
//    private String ttt(String qq){
//        System.out.println();
//        return "aa";

//    };


    public static void main(String[] args) {
        TestChild child = new TestChild();

        child.ttt("dsds");
    }
}

