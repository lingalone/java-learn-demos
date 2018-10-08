package io.github.lingalone;

import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/9/11
 */

public class ListTest {
    public static void traverse(List list){

        if (list instanceof RandomAccess){
            System.out.println("实现了RandomAccess接口，不使用迭代器");

            for (int i = 0;i < list.size();i++){
                System.out.println(list.get(i));
            }

        }else{
            System.out.println("没实现RandomAccess接口，使用迭代器");

            Iterator it = list.iterator();
            while(it.hasNext()){
                System.out.println(it.next());
            }

        }
    }
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<String>();
        arrayList.add("a");
        arrayList.add("b");
        traverse(arrayList);

        List<String> linkedList = new LinkedList<String>();
        linkedList.add("c");
        linkedList.add("d");
        traverse(linkedList);
    }


    @Test
    public void test1(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        for (String item: list) {
            if (item.equals("4")) {
                list.remove("4"); //删除的不一定是当前元素
                list.remove("5"); //删除的不一定是当前元素
            }
        }
        System.out.println(Arrays.toString(list.toArray()));
    }
}
