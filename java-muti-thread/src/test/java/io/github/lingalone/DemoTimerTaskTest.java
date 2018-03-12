package io.github.lingalone;

import org.junit.Test;

import java.util.Date;
import java.util.Timer;

import static org.junit.Assert.*;

public class DemoTimerTaskTest {

    @Test
    public void DemoTest(){
        Timer timer = new Timer();
        timer.schedule(new DemoTimerTask(), 2000);

        while(true) {
            System.out.println(new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}