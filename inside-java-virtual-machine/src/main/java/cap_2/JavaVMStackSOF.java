package cap_2;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/10/22
 */
// -Xss128k
public class JavaVMStackSOF {

    private int stackLength  = 1;


    public void stackLeak(){
        stackLength ++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF stackSOF = new JavaVMStackSOF();
        try {
            stackSOF.stackLeak();
        }catch (Throwable e){
            System.out.println("stack length :" + stackSOF.stackLength);
            throw e;
        }
    }
}
