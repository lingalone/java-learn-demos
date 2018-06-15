package io.github.lingalone.javafonttools.temp;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/14
 */


public class WoffConverter {

    @SuppressWarnings("serial")
    private static final LinkedHashMap<String, Integer> woffHeaderFormat = new LinkedHashMap<String, Integer>() {
        {
            put("signature", 4);
            put("flavor", 4);
            put("length", 4);
            put("numTables", 2);
            put("reserved", 2);
            put("totalSfntSize", 4);
            put("majorVersion", 2);
            put("minorVersion", 2);
            put("metaOffset", 4);
            put("metaLength", 4);
            put("metaOrigLength", 4);
            put("privOffset", 4);
            put("privOrigLength", 4);
        }
    };

    @SuppressWarnings("serial")
    private static final LinkedHashMap<String, Integer> tableRecordEntryFormat = new LinkedHashMap<String, Integer>() {
        {
            put("tag", 4);
            put("offset", 4);
            put("compLength", 4);
            put("origLength", 4);
            put("origChecksum", 4);
        }
    };

    private HashMap<String, Number> woffHeaders = new HashMap<String, Number>();

    private ArrayList<HashMap<String, Number>> tableRecordEntries = new ArrayList<HashMap<String, Number>>();



    private Map<String, String> glyphCode = new HashMap<>();

    private int offset = 0;

    private int readOffset = 0;

    public Map<String, String> getGlyphCode() {
        return glyphCode;
    }

    public byte[] convertToTTFByteArray(InputStream woffFileStream)
            throws InvalidWoffException, IOException, DataFormatException {
        ByteArrayOutputStream ttfOutputStream = convertToTTFOutputStream(woffFileStream);
        byte[] ttfByteArray = ttfOutputStream.toByteArray();
        return ttfByteArray;
    }

    public ByteArrayOutputStream convertToTTFOutputStream(InputStream woffFileStream)
            throws InvalidWoffException, IOException, DataFormatException {
        getHeaders(new DataInputStream(woffFileStream));
        if ((Integer) woffHeaders.get("signature") != 0x774F4646) {
            throw new InvalidWoffException("Invalid woff file");
        }
        System.out.println("WoffHeader-offset " + readOffset);
        ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
        writeOffsetTable(ttfOutputStream);
        getTableRecordEntries(new DataInputStream(woffFileStream));
        System.out.println("TableRecord-offset " + readOffset);
        writeTableRecordEntries(ttfOutputStream);
        writeFontData(woffFileStream, ttfOutputStream);
        return ttfOutputStream;
    }

    private void getHeaders(DataInputStream woffFileStream) throws IOException {
        readTableData(woffFileStream, woffHeaderFormat, woffHeaders);

        System.out.println(woffHeaders.toString());
    }

    private void writeOffsetTable(ByteArrayOutputStream ttfOutputStream)
            throws IOException {
        ttfOutputStream.write(getBytes((Integer) woffHeaders.get("flavor")));
        int numTables = (Integer) woffHeaders.get("numTables");
        ttfOutputStream.write(getBytes((short)numTables));
        int temp = numTables;
        int searchRange = 16;
        short entrySelector = 0;
        while (temp > 1) {
            temp = temp >> 1;
            entrySelector++;
            searchRange = (searchRange << 1);
        }
        short rangeShift = (short) (numTables * 16 - searchRange);
        ttfOutputStream.write(getBytes((short) searchRange));
        ttfOutputStream.write(getBytes(entrySelector));
        ttfOutputStream.write(getBytes(rangeShift));
        offset += 12;
    }

    private void getTableRecordEntries(DataInputStream woffFileStream)
            throws IOException {
        int numTables = (Integer) woffHeaders.get("numTables");
        for (int i = 0; i < numTables; i++) {
            HashMap<String, Number> tableDirectory = new HashMap<String, Number>();
            readTableData(woffFileStream, tableRecordEntryFormat,
                    tableDirectory);
            offset += 16;
            tableRecordEntries.add(tableDirectory);
            System.out.println(tableDirectory.toString());
        }
    }

    private void writeTableRecordEntries(ByteArrayOutputStream ttfOutputStream)
            throws IOException {
        for (HashMap<String, Number> tableRecordEntry : tableRecordEntries) {
            ttfOutputStream.write(getBytes((Integer) tableRecordEntry
                    .get("tag")));
            ttfOutputStream.write(getBytes((Integer) tableRecordEntry
                    .get("origChecksum")));
            ttfOutputStream.write(getBytes(offset));
            ttfOutputStream.write(getBytes((Integer) tableRecordEntry
                    .get("origLength")));
            tableRecordEntry.put("outOffset", offset);
            offset += (Integer) tableRecordEntry.get("origLength");
            if (offset % 4 != 0) {
                offset += 4 - (offset % 4);
            }
        }
    }

    private void writeFontData(InputStream woffFileStream,
                               ByteArrayOutputStream ttfOutputStream) throws IOException,
            DataFormatException {
        for (HashMap<String, Number> tableRecordEntry : tableRecordEntries) {
            int tableRecordEntryOffset = (Integer) tableRecordEntry
                    .get("offset");
            int skipBytes = tableRecordEntryOffset - readOffset;
            if (skipBytes > 0)
                woffFileStream.skip(skipBytes);
            readOffset += skipBytes;
            int compressedLength = (Integer) tableRecordEntry.get("compLength");
            int origLength = (Integer) tableRecordEntry.get("origLength");
            byte[] fontData = new byte[compressedLength];
            byte[] inflatedFontData = new byte[origLength];
            int readBytes = 0;
            while (readBytes < compressedLength) {
                readBytes += woffFileStream.read(fontData, readBytes,
                        compressedLength - readBytes);
            }
            readOffset += compressedLength;
            inflatedFontData = inflateFontData(compressedLength,
                    origLength, fontData, inflatedFontData);

            //
            System.out.println(inflatedFontData.length);

            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(inflatedFontData));
            int cmpIndex = 0;
            Integer version = dataInputStream.readUnsignedShort();
            Integer numTables = dataInputStream.readUnsignedShort();
            System.out.println("version   -  "+version);
            if(version==0 && numTables< 14 && numTables > 0){
                cmpIndex += 4;
                System.out.println("numTables -  "+numTables);

                int offset12 = 0;
                for(int i=0;i<numTables;i++){
                    Integer platformID = dataInputStream.readUnsignedShort();
                    Integer encodingID = dataInputStream.readUnsignedShort();
                    Integer offset2 = dataInputStream.readInt();

                    System.out.println("platformID   -  "+platformID);
                    System.out.println("encodingID   -  "+encodingID);
                    System.out.println("offset       -  "+offset2);
                    if(platformID==3 && encodingID==10)
                        offset12 = offset2;
                    cmpIndex += 8;
                }
                dataInputStream.skipBytes(offset12 - cmpIndex);

                Integer format = dataInputStream.readUnsignedShort();
                Integer reserved = dataInputStream.readUnsignedShort();
                Integer length = dataInputStream.readInt();
                Integer language = dataInputStream.readInt();
                Integer numGroups = dataInputStream.readInt();

                System.out.println("format          -  "+format);
                System.out.println("reserved        -  "+reserved);
                System.out.println("length          -  "+length);
                System.out.println("language        -  "+language);
                System.out.println("numGroups       -  "+numGroups);

                if(format==12 && reserved==0){
                    for(int ii=0;ii<numGroups;ii++){
                        Integer startCharCode = dataInputStream.readInt();
                        Integer endCharCode = dataInputStream.readInt();
                        Integer startGlyphID = dataInputStream.readInt();

                        System.out.println("startCharCode           -  "+    startCharCode);
                        System.out.println("endCharCode             -  "+    endCharCode);
                        System.out.println("startGlyphID            -  "+    startGlyphID);
                        char res = (char)((int) startCharCode);
                        System.out.println("------------            -  "+    res);
                        glyphCode.put(Character.toString(res), Character.toString(res));
                    }
                }
            }


//            System.out.println("-"+dataInputStream.readUnsignedShort());
//            System.out.println(dataInputStream.readUnsignedShort());
//            System.out.println(dataInputStream.readUnsignedShort());
//            System.out.println(dataInputStream.readInt());
//            System.out.println(dataInputStream.readInt());
//            System.out.println(dataInputStream.readInt());
//            System.out.println(dataInputStream.readUnsignedShort());



//            String str = new String(inflatedFontData);
//            System.out.println(str);



            ttfOutputStream.write(inflatedFontData);
            offset = (Integer) tableRecordEntry.get("outOffset")
                    + (Integer) tableRecordEntry.get("origLength");
            int padding = 0;
            if (offset % 4 != 0)
                padding = 4 - (offset % 4);
            ttfOutputStream.write(getBytes(0), 0, padding);
        }
    }


    private byte[] inflateFontData(int compressedLength, int origLength,
                                   byte[] fontData, byte[] inflatedFontData) {
        if (compressedLength != origLength) {
            Inflater decompressor = new Inflater();
            decompressor.setInput(fontData, 0, compressedLength);
            try {
                decompressor.inflate(inflatedFontData, 0, origLength);
            } catch (DataFormatException e) {
                throw new InvalidWoffException("Malformed woff file");
            }
        } else
            inflatedFontData = fontData;
        return inflatedFontData;
    }

    private byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    private byte[] getBytes(short h) {
        return ByteBuffer.allocate(2).putShort(h).array();
    }

    private void readTableData(DataInputStream woffFileStream,
                               LinkedHashMap<String, Integer> formatTable,
                               HashMap<String, Number> table) throws IOException {
        Iterator<String> headerKeys = formatTable.keySet().iterator();
        while (headerKeys.hasNext()) {
            String key = headerKeys.next();
            int size = formatTable.get(key);
            if (size == 2) {
                table.put(key, woffFileStream.readUnsignedShort());
            } else if (size == 4) {
                table.put(key, woffFileStream.readInt());
            }
            readOffset += size;
        }
    }
}