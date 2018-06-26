package io.github.lingalone.javafonttools.font;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class WoffProccessTest {


    @Test
    public void createWoff() throws IOException, FontFormatException {
        WoffProccess woffProccess = new WoffProccess("C:\\Users\\owner\\Downloads\\tyc-num.woff");
        System.out.println(woffProccess.getWoff().toString());

        TTFProccess ttfProccess = new TTFProccess();
        FontSaver.saveTTF("C:\\Users\\owner\\Downloads\\c052136_ISO_IEC_14496-22_2009(E)\\ggg.ttf",FontTransfer.woffToTTF(woffProccess.getWoff()));
        FontSaver.saveWoff("C:\\Users\\owner\\Downloads\\c052136_ISO_IEC_14496-22_2009(E)\\ggg.woff",woffProccess.getWoff());

        FontImage fontImage = new FontImage();
        fontImage.setFontFile("C:\\Users\\owner\\Downloads\\c052136_ISO_IEC_14496-22_2009(E)\\ggg.ttf");
        fontImage.setFontSize(80);
        fontImage.setImgSize(100);
        fontImage.setSaveDir("E:\\font");
        fontImage.setFontKeys(new ArrayList<String>(woffProccess.woff.getGlyphCode().values()));
        FontToImage.saveFontImageSingle(fontImage);

    }


    @Test
    public void fetch() throws IOException, FontFormatException {

        File file = Paths.get("E:\\font\\woff").toFile();
        if(file.isDirectory()){
            for (File subFile: file.listFiles()) {
                System.out.println(subFile.getPath());
                if(subFile.exists()){
                    WoffProccess woffProccess = new WoffProccess(subFile);
                    TTFProccess ttfProccess = new TTFProccess();
                    FontSaver.saveTTF("E:\\font\\ttf\\"+subFile.getName()+".ttf", FontTransfer.woffToTTF(woffProccess.getWoff()));


                    FontImage fontImage = new FontImage();
                    fontImage.setFontFile("E:\\font\\ttf\\"+subFile.getName()+".ttf");
                    fontImage.setFontSize(80);
                    fontImage.setImgSize(100);
                    fontImage.setSaveDir("E:\\font\\img");
                    fontImage.setFontKeys(new ArrayList<String>(woffProccess.woff.getGlyphCode().values()));
                    FontToImage.saveFontImageSingle(fontImage);
                }
            }
        }

    }

}