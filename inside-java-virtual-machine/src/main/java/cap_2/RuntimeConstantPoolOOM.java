package cap_2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/10/22
 */

public class RuntimeConstantPoolOOM {



    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();

        int i = 0;
        while(true){
            list.add(String.valueOf(i++).intern());
        }

    }
}
