package io.github.lingalone.javafonttools.font;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WoffProccessTest {


    @Test
    public void createWoff() throws IOException {
        WoffProccess woffProccess = new WoffProccess("C:\\Users\\owner\\Downloads\\tyc-num.woff");
        System.out.println(woffProccess.getWoff().toString());

        TTFProccess ttfProccess = new TTFProccess();
        ttfProccess.saveTTF("C:\\Users\\owner\\Downloads\\c052136_ISO_IEC_14496-22_2009(E)\\ggg.ttf",FontTransfer.woffToTTF(woffProccess.getWoff()));

    }

}