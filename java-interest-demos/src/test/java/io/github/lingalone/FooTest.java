package io.github.lingalone;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/9/13
 */

public class FooTest {

  static boolean foo(char a){
      System.out.println(a);
      return false;
  }


    public static void main(String[] args) {
        int i =0;
        for (foo('A');foo('B')&&(i<2);foo('c')){
            i++;
            foo('D');
        }
    }
}
