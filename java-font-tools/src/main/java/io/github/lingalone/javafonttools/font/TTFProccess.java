package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.opentype.Cmap;
import io.github.lingalone.javafonttools.font.ttf.TTF;
import io.github.lingalone.javafonttools.font.ttf.TTFHeader;
import io.github.lingalone.javafonttools.font.ttf.TableRecord;
import io.github.lingalone.javafonttools.font.woff.InvalidFontException;
import io.github.lingalone.javafonttools.font.woff.TableDirectory;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */

public class TTFProccess {


    TTF ttf = new TTF();

    public TTF getTtf() {
        return ttf;
    }

    public TTFProccess(String woffPath) {
        try {

            byte[] woffFile = IOUtils.toByteArray(new FileInputStream(woffPath));
            ttf.setFontFile(woffFile);
            this.getTTFHeader();
            this.getTableRecords();
            this.getGlyphCode();

        } catch (IOException e) {

        }
    }
    TTFHeader getTTFHeader() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(ttf.getFontFile()));
        int sfntVersion = dataInputStream.readInt();
        int numTables = dataInputStream.readUnsignedShort();
        int searchRange = dataInputStream.readUnsignedShort();
        int entrySelector = dataInputStream.readUnsignedShort();
        int rangeShift = dataInputStream.readUnsignedShort();

        TTFHeader ttfHeader = new TTFHeader();
        ttfHeader.setSfntVersion(sfntVersion);
        ttfHeader.setNumTables(numTables);
        ttfHeader.setSearchRange(searchRange);
        ttfHeader.setEntrySelector(entrySelector);
        ttfHeader.setRangeShift(rangeShift);

        dataInputStream.close();

        if (0x00010000 != sfntVersion && 0x4F54544F != sfntVersion){
            throw new InvalidFontException("Invalid ttf file");
        }
        int ttfHeaderOffset = 12;
        int tableRecordsOffset = ttfHeaderOffset + numTables * 16;

        ttf.setTtfHeader(ttfHeader);
        ttf.setTableNum(numTables);
        ttf.setTtfHeaderOffset(ttfHeaderOffset);
        ttf.setTableRecordsOffset(tableRecordsOffset);
        return ttfHeader;
    }

    List<TableRecord> getTableRecords() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(ttf.getFontFile()));
        dataInputStream.skipBytes(ttf.getTtfHeaderOffset());
        List<TableRecord> tableRecords = new ArrayList<>(ttf.getTableNum());
        for (int i = 0; i < ttf.getTableNum(); i++){
            int tag = dataInputStream.readInt();
            int checkSum = dataInputStream.readInt();
            int offset = dataInputStream.readInt();
            int length = dataInputStream.readInt();
            TableRecord tableRecord = new TableRecord();
            tableRecord.setTag(tag);
            tableRecord.setCheckSum(checkSum);
            tableRecord.setOffset(offset);
            tableRecord.setLength(length);
            tableRecords.add(tableRecord);
        }
        ttf.setTableRecords(tableRecords);
        return tableRecords;
    }


    Map<Integer, String> getGlyphCode() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(ttf.getFontFile()));
        int readOffset = ttf.getTableRecordsOffset();

        List<byte[]> tableData = new ArrayList<>();
        Map<Integer, String> glyphCode = new HashMap<>();
        dataInputStream.skip(readOffset);
        try {
            for(TableRecord record : ttf.getTableRecords()){
                dataInputStream = new DataInputStream(new ByteArrayInputStream(ttf.getFontFile()));

                System.out.println(record.getOffset());
                System.out.println(record.toString());

                dataInputStream.skipBytes(record.getOffset());

                byte[] fontData = new byte[record.getLength()];
                int readBytes = 0;
                while (readBytes < record.getLength()) {
                    readBytes += dataInputStream.read(fontData, readBytes,
                            record.getLength() - readBytes);
                }
                System.out.println(fontData.length);

                Map<Integer, String> temp;
                try{
                    temp = Cmap.getMap(fontData);
                    if(temp!=null && temp.size()>1){
                        glyphCode = temp;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                tableData.add(fill(fontData));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        ttf.setGlyphCode(glyphCode);
        ttf.setTableData(tableData);
        return glyphCode;
    }

    private byte[] fill(byte[] fontData) throws IOException {
        ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
        ttfOutputStream.write(fontData);
        int padding = 0;
        if (fontData.length % 4 != 0)
            padding = 4 - (fontData.length % 4);
        ttfOutputStream.write(getBytes(0), 0, padding);

        byte[] res = ttfOutputStream.toByteArray();
        ttfOutputStream.close();
        return res;
    }

    private byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    private byte[] getBytes(short h) {
        return ByteBuffer.allocate(2).putShort(h).array();
    }


    private Map<Integer,String> getMap(byte [] inflatedFontData) throws IOException {
        Map<Integer, String> glyphCode = new HashMap<>();
        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(inflatedFontData));
        int cmpIndex = 0;
        Integer version = dataInputStream2.readUnsignedShort();
        Integer numTables = dataInputStream2.readUnsignedShort();
        System.out.println("version   -  "+version);
        if(version==0 && numTables< 14 && numTables > 0){
            cmpIndex += 4;
            System.out.println("numTables -  "+numTables);

            int offset12 = 0;
            for(int i=0;i<numTables;i++){
                Integer platformID = dataInputStream2.readUnsignedShort();
                Integer encodingID = dataInputStream2.readUnsignedShort();
                Integer offset2 = dataInputStream2.readInt();

                System.out.println("platformID   -  "+platformID);
                System.out.println("encodingID   -  "+encodingID);
                System.out.println("offset       -  "+offset2);
                if(platformID==3 && encodingID==10)
                    offset12 = offset2;
                cmpIndex += 8;
            }
            dataInputStream2.skipBytes(offset12 - cmpIndex);

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
                return glyphCode;
            }
        }
        return  null;
    }


}
