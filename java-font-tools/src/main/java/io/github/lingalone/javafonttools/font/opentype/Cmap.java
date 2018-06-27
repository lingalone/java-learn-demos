package io.github.lingalone.javafonttools.font.opentype;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link https://docs.microsoft.com/en-us/typography/opentype/spec/cmap
 * @since 2018/6/26
 */

public class Cmap {

//    public getFormat

    public static Map<Integer,String> getMap(byte [] fontData) throws IOException {
        Map<Integer, String> glyphCode = null;
        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(fontData));

        Integer version = dataInputStream2.readUnsignedShort();
        Integer numTables = dataInputStream2.readUnsignedShort();

        System.out.println("version   -  "+version);
        if(version==0 && numTables< 14 && numTables > 0){
            System.out.println("numTables -  "+numTables);

            for(int i=0;i<numTables;i++) {
                Integer platformID = dataInputStream2.readUnsignedShort();
                Integer encodingID = dataInputStream2.readUnsignedShort();
                Integer offset2 = dataInputStream2.readInt();

                System.out.println("platformID   -  " + platformID);
                System.out.println("encodingID   -  " + encodingID);
                System.out.println("offset       -  " + offset2);


                //format 14  == platform ID 0 and encoding ID 5
                // format 4  == platform ID 3 and encoding ID 1
                //format 12  == platform ID 3 and encoding ID 10
                //platform ID 1 and encoding ID 0
                //platform ID 3 and encoding ID 1   Unicode
                //platform ID 3 and encoding ID 0   symbol

                if (platformID == 3 && encodingID == 10){
                    System.out.println("format 12");
                    glyphCode = format_12(fontData, offset2);
                    if(glyphCode!=null){
                        return glyphCode;
                    }
                }else if(platformID == 3 && encodingID == 1){
                    System.out.println("format 4");
                    glyphCode = format_4(fontData, offset2);
                    if(glyphCode!=null){
                        return glyphCode;
                    }
                }else if(platformID == 0 && encodingID == 5){
                    System.out.println("format 14");
                    glyphCode = format_14(fontData, offset2);
                    if(glyphCode!=null){
                        return glyphCode;
                    }
                }

            }

        }
        dataInputStream2.close();
        return  null;
    }

    static Map<Integer, String> format_12(byte [] fontData, Integer offset) throws IOException {
        Map<Integer, String> glyphCode = new HashMap<>();
        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(fontData));
        dataInputStream2.skipBytes(offset);

        Integer format = dataInputStream2.readUnsignedShort();
        Integer reserved = dataInputStream2.readUnsignedShort();
        Integer length = dataInputStream2.readInt();
        Integer language = dataInputStream2.readInt();
        Integer numGroups = dataInputStream2.readInt();

        System.out.println("format          -  "+format);
        System.out.println("reserved        -  "+reserved);
        System.out.println("length          -  "+length);
        System.out.println("language        -  "+language);
        System.out.println("numGroups       -  "+numGroups);

        if(format==12 && reserved==0){
            for(int ii=0;ii<numGroups;ii++){
                Integer startCharCode = dataInputStream2.readInt();
                Integer endCharCode = dataInputStream2.readInt();
                Integer startGlyphID = dataInputStream2.readInt();

                System.out.println("startCharCode           -  "+    startCharCode);
                System.out.println("endCharCode             -  "+    endCharCode);
                System.out.println("startGlyphID            -  "+    startGlyphID);
                char res = (char)((int) startCharCode);
                System.out.println("------------            -  "+    res);
                glyphCode.put(startGlyphID, Character.toString(res));
            }
            dataInputStream2.close();
            return glyphCode;
        }
        dataInputStream2.close();
        return null;
    }

    static Map<Integer, String> format_14(byte [] fontData, Integer offset) throws IOException {
        Map<Integer, String> glyphCode = new HashMap<>();
        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(fontData));
        dataInputStream2.skipBytes(offset);

        Integer format = dataInputStream2.readUnsignedShort();
        Integer length = dataInputStream2.readInt();
        Integer numVarSelectorRecords = dataInputStream2.readInt();

        System.out.println("format                          -  "+format);
        System.out.println("length                          -  "+length);
        System.out.println("numVarSelectorRecords           -  "+numVarSelectorRecords);

        if(format==14){
            for(int ii=0;ii<numVarSelectorRecords;ii++){
                Integer varSelector = dataInputStream2.skipBytes(3);
                Integer defaultUVSOffset = dataInputStream2.readInt();
                Integer nonDefaultUVSOffset = dataInputStream2.readInt();

                System.out.println("varSelector                     -  "+    varSelector);
                System.out.println("defaultUVSOffset                -  "+    defaultUVSOffset);
                System.out.println("nonDefaultUVSOffset             -  "+    nonDefaultUVSOffset);
//                char res = (char)((int) startCharCode);
//                System.out.println("------------            -  "+    res);
//                glyphCode.put(startGlyphID, Character.toString(res));
            }
            dataInputStream2.close();
            return glyphCode;
        }
        dataInputStream2.close();
        return null;
    }

    static Map<Integer, String> format_4(byte [] fontData, Integer offset) throws IOException {
        Map<Integer, String> glyphCode = new HashMap<>();
        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(fontData));
        dataInputStream2.skipBytes(offset);

        Integer format = dataInputStream2.readUnsignedShort();
        Integer length = dataInputStream2.readUnsignedShort();
        Integer language = dataInputStream2.readUnsignedShort();
        Integer segCountX2 = dataInputStream2.readUnsignedShort();
        Integer segCount = segCountX2/2;
        Integer searchRange = dataInputStream2.readUnsignedShort();
        Integer entrySelector = dataInputStream2.readUnsignedShort();
        Integer rangeShift = dataInputStream2.readUnsignedShort();

        Integer[] endCount = new Integer[segCount];
        for (int i = 0; i < segCount; i++) {
            endCount[i] = dataInputStream2.readUnsignedShort();
        }
        Integer reservedPad = dataInputStream2.readUnsignedShort();
        Integer[] startCount = new Integer[segCount];
        for (int i = 0; i < segCount; i++) {
            startCount[i] = dataInputStream2.readUnsignedShort();
        }

        Short[] idDelta = new Short[segCount];
        for (int i = 0; i < segCount; i++) {
            idDelta[i] = dataInputStream2.readShort();
        }

        Integer[] idRangeOffset = new Integer[segCount];
        for (int i = 0; i < segCount; i++) {
            idRangeOffset[i] = dataInputStream2.readUnsignedShort();
        }



//        Integer numVarSelectorRecords = dataInputStream2.readInt();

        System.out.println("format                          -  "+format);
        System.out.println("length                          -  "+length);
        System.out.println("language                        -  "+language);
        System.out.println("segCountX2                      -  "+segCountX2);
        System.out.println("searchRange                     -  "+searchRange);
        System.out.println("entrySelector                   -  "+entrySelector);
        System.out.println("rangeShift                      -  "+rangeShift);
        System.out.println("endCount                        -  "+ Arrays.toString(endCount));
        System.out.println("reservedPad                     -  "+reservedPad);
        System.out.println("startCount                      -  "+Arrays.toString(startCount));
        System.out.println("idDelta                         -  "+Arrays.toString(idDelta));
        System.out.println("idRangeOffset                   -  "+Arrays.toString(idRangeOffset));
        System.out.println("reservedPad                     -  "+reservedPad);



        Set<Integer> chIndex = new TreeSet<>();
        Set<Character> ch = new TreeSet<>();
        for (int i = 0; i < segCount; i++) {
            if(startCount[i]!=0Xffff){
                chIndex.add(startCount[i] - idDelta[i]);
                chIndex.add(endCount[i] - idDelta[i]);

                Integer tempCount = endCount[i]-startCount[i]+1;


                for (int j = 0; j < tempCount; j++) {
                    chIndex.add(startCount[i] + j);
//                    System.out.println((char) (startCount[i] + j));
                    ch.add((char) (startCount[i] + j));
                }
//                if(idRangeOffset[i]!=0){
////                    System.out.println("---------------------s-------------------");
//                    for (int j = 0; j < tempCount; j++) {
//                        DataInputStream temp = new DataInputStream(new ByteArrayInputStream(fontData));
//                        temp.skipBytes(j+idRangeOffset[i]);
//                        int rr = temp.readUnsignedShort();
//                        chIndex.add(rr);
//                        System.out.println((char) (rr));
//                        ch.add((char) (rr));
//                    }
////                    System.out.println("---------------------e-------------------");
//                }else{
//                    for (int j = 0; j < tempCount; j++) {
//                        chIndex.add(startCount[i] + j);
//                        System.out.println((char) (startCount[i] + j));
//                        ch.add((char) (startCount[i] + j));
//                    }
//                }

            }
        }
        System.out.println("chIndex                       -  "+chIndex.toString());
        System.out.println("chIndexSize                   -  "+chIndex.size());
        System.out.println("chSize                        -  "+ch.size());

        if(chIndex == null || chIndex.isEmpty()){
            dataInputStream2.close();
            return null;
        }
        chIndex.forEach( integer ->  {
            glyphCode.put(integer, Character.toString((char)((int) integer)));
        });
        dataInputStream2.close();
        return glyphCode;

    }


}
