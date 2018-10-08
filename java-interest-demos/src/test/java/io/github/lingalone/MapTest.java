package io.github.lingalone;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/9/4
 */

public class MapTest {

    @Test
    public void autoScan1(){
        Map<String,String> map = new HashMap<String, String>();
        for (int i = 0; i < 10000000; i++) {
            map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }
    }
    @Test
    public void autoScan2(){
        Map<String,String> map = new HashMap<String, String>(10000000);
        for (int i = 0; i < 10000000; i++) {
            map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }
    }
}
