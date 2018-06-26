package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.ttf.TTF;
import io.github.lingalone.javafonttools.font.ttf.TableRecord;
import io.github.lingalone.javafonttools.font.woff.TableDirectory;
import io.github.lingalone.javafonttools.font.woff.Woff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/26
 */

public class FontSaver {

    public static void saveTTF(String filePath, TTF ttf) throws IOException {
        System.out.println(ttf.getTtfHeader().toString());
        ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
        ttfOutputStream.write(getBytes((int)  ttf.getTtfHeader().getSfntVersion()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getNumTables()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getSearchRange()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getEntrySelector()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getRangeShift()));

        for (TableRecord tableRecord : ttf.getTableRecords()) {
            System.out.println(tableRecord.toString());
            ttfOutputStream.write(getBytes((int) tableRecord.getTag()));
            ttfOutputStream.write(getBytes((int) tableRecord.getCheckSum()));
            ttfOutputStream.write(getBytes((int) tableRecord.getOffset()));
            ttfOutputStream.write(getBytes((int) tableRecord.getLength()));
        }

        for (byte[] data : ttf.getTableData()){
            ttfOutputStream.write(data);
        }
        FileOutputStream stream = new FileOutputStream(new File(filePath));
        stream.write(ttfOutputStream.toByteArray());
        stream.close();
    }


    public static void saveWoff(String filePath, Woff woff) throws IOException {

        System.out.println(woff.getWoffHeader().toString());
        ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getSignature()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getFlavor()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getLength()));
        ttfOutputStream.write(getBytes((short) woff.getWoffHeader().getNumTables()));
        ttfOutputStream.write(getBytes((short) woff.getWoffHeader().getReserved()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getTotalSfntSize()));
        ttfOutputStream.write(getBytes((short) woff.getWoffHeader().getMajorVersion()));
        ttfOutputStream.write(getBytes((short) woff.getWoffHeader().getMinorVersion()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getMetaOffset()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getMetaLength()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getMetaOrigLength()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getPrivOffset()));
        ttfOutputStream.write(getBytes((int)  woff.getWoffHeader().getPrivLength()));


        for (TableDirectory directory : woff.getTableDirectories()) {
            System.out.println(directory.toString());
            ttfOutputStream.write(getBytes((int) directory.getTag()));
            ttfOutputStream.write(getBytes((int) directory.getOffset()));
            ttfOutputStream.write(getBytes((int) directory.getCompLength()));
            ttfOutputStream.write(getBytes((int) directory.getOrigLength()));
            ttfOutputStream.write(getBytes((int) directory.getOrigChecksum()));
        }

        for (byte[] data : woff.getCompTableData()){
            ttfOutputStream.write(data);
        }
        FileOutputStream stream = new FileOutputStream(new File(filePath));
        stream.write(ttfOutputStream.toByteArray());
        stream.close();
    }






    private static byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }
    private static byte[] getBytes(short i) {
        System.out.println(i);
        return ByteBuffer.allocate(2).putShort(i).array();
    }
}
