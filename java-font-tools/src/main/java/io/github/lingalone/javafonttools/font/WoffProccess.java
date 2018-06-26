package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.woff.InvalidFontException;
import io.github.lingalone.javafonttools.font.woff.TableDirectory;
import io.github.lingalone.javafonttools.font.woff.Woff;
import io.github.lingalone.javafonttools.font.woff.WoffHeader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */

public class WoffProccess {


    Woff woff = new Woff();

    public WoffProccess(String woffPath) {
        try {

            byte[] woffFile = IOUtils.toByteArray(new FileInputStream(woffPath));
            woff.setFontFile(woffFile);
            this.getWoffHeader();
            this.getTableDirectories();
            this.getGlyphCode();
        } catch (IOException e) {

        }
    }

    public WoffProccess(File file) {
        try {

            byte[] woffFile = IOUtils.toByteArray(new FileInputStream(file));
            woff.setFontFile(woffFile);
            this.getWoffHeader();
            this.getTableDirectories();
            this.getGlyphCode();
        } catch (IOException e) {

        }
    }


    public Woff getWoff() {
        return woff;
    }

    WoffHeader getWoffHeader() throws IOException {

        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(woff.getFontFile()));

        int     signature =     dataInputStream.readInt();
        int     flavor =        dataInputStream.readInt();
        int     length =        dataInputStream.readInt();
        int     numTables =     dataInputStream.readUnsignedShort();
        int     reserved =      dataInputStream.readUnsignedShort();
        int     totalSfntSize = dataInputStream.readInt();
        int     majorVersion =  dataInputStream.readUnsignedShort();
        int     minorVersion =  dataInputStream.readUnsignedShort();
        int     metaOffset =    dataInputStream.readInt();
        int     metaLength =    dataInputStream.readInt();
        int     metaOrigLength =dataInputStream.readInt();
        int     privOffset =    dataInputStream.readInt();
        int     privLength =    dataInputStream.readInt();

        WoffHeader woffHeader = new WoffHeader();
        woffHeader.setSignature(signature);
        woffHeader.setFlavor(flavor);
        woffHeader.setLength(length);
        woffHeader.setNumTables(numTables);
        woffHeader.setReserved(reserved);
        woffHeader.setTotalSfntSize(totalSfntSize);
        woffHeader.setMajorVersion(majorVersion);
        woffHeader.setMinorVersion(minorVersion);
        woffHeader.setMetaOffset(metaOffset);
        woffHeader.setMetaLength(metaLength);
        woffHeader.setMetaOrigLength(metaOrigLength);
        woffHeader.setPrivOffset(privOffset);
        woffHeader.setPrivLength(privLength);

        dataInputStream.close();

        if (0x774F4646 != signature){
            throw new InvalidFontException("Invalid woff file");
        }

        int woffHeaderOffset = 4*11;
        int tableDirectoriesOffset = woffHeaderOffset + 20 * numTables;

        woff.setWoffHeaderOffset(woffHeaderOffset);
        woff.setTableDirectoriesOffset(tableDirectoriesOffset);
        woff.setTableNum(numTables);
        woff.setWoffHeader(woffHeader);
        return woffHeader;
    }

    List<TableDirectory> getTableDirectories() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(woff.getFontFile()));

        dataInputStream.skipBytes(woff.getWoffHeaderOffset());
        List<TableDirectory> tableDirectories = new ArrayList<>(woff.getTableNum());
        for (int i = 0; i < woff.getTableNum(); i++) {

            int tag =           dataInputStream.readInt();
            int offset =        dataInputStream.readInt();
            int compLength =    dataInputStream.readInt();
            int origLength =    dataInputStream.readInt();
            int origChecksum =  dataInputStream.readInt();

            TableDirectory tableDirectory = new TableDirectory();
            tableDirectory.setTag(tag);
            tableDirectory.setOffset(offset);
            tableDirectory.setCompLength(compLength);
            tableDirectory.setOrigLength(origLength);
            tableDirectory.setOrigChecksum(origChecksum);
            tableDirectories.add(tableDirectory);
        }
        woff.setTableDirectories(tableDirectories);
        return tableDirectories;
    }


    Map<Integer, String> getGlyphCode() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(woff.getFontFile()));
        int readOffset = woff.getTableDirectoriesOffset();

        List<byte[]> tableData = new ArrayList<>();
        Map<Integer, String> glyphCode = new HashMap<>();
        dataInputStream.skip(readOffset);
        for(TableDirectory directory : woff.getTableDirectories()){
            System.out.println(directory.getOffset());
            System.out.println(directory.toString());
            int skipBytes = directory.getOffset() - readOffset;
            if (skipBytes > 0)
                dataInputStream.skip(skipBytes);
            readOffset += skipBytes;

            byte[] fontData = new byte[directory.getCompLength()];
            byte[] inflatedFontData = new byte[directory.getOrigLength()];
            int readBytes = 0;
            while (readBytes < directory.getCompLength()) {
                readBytes += dataInputStream.read(fontData, readBytes,
                        directory.getCompLength() - readBytes);
            }
            System.out.println(fontData.length);
            readOffset += directory.getCompLength();

            inflatedFontData = inflateFontData(directory.getCompLength(),
                    directory.getOrigLength(), fontData, inflatedFontData);
            System.out.println(inflatedFontData.length);

            Map<Integer, String> temp = getMap(inflatedFontData);
            if(temp!=null){
                glyphCode = temp;
            }

            ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
            ttfOutputStream.write(inflatedFontData);
            int offset = directory.getOffset() + directory.getOrigLength();
            int padding = 0;
            if (offset % 4 != 0)
                padding = 4 - (offset % 4);
            ttfOutputStream.write(getBytes(0), 0, padding);
            tableData.add(ttfOutputStream.toByteArray());
            ttfOutputStream.close();

        }
        woff.setGlyphCode(glyphCode);
        woff.setTableData(tableData);
        return glyphCode;
    }

    private byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    private byte[] getBytes(short h) {
        return ByteBuffer.allocate(2).putShort(h).array();
    }


    private byte[] inflateFontData(int compressedLength, int origLength,
                                   byte[] fontData, byte[] inflatedFontData) {
        if (compressedLength != origLength) {
            Inflater decompressor = new Inflater();
            decompressor.setInput(fontData, 0, compressedLength);
            try {
                decompressor.inflate(inflatedFontData, 0, origLength);
            } catch (DataFormatException e) {
                throw new InvalidFontException("Malformed woff file");
            }
        } else
            inflatedFontData = fontData;
        return inflatedFontData;
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
