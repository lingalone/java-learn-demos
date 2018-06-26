package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.ttf.TableRecord;

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
 * @since 2018/6/15
 */

public class TTFProccess {


    public void saveTTF(String filePath, TTF ttf) throws IOException {
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

        int count = 0;
        for (byte[] data : ttf.getTableData()){
            ttfOutputStream.write(data);
        }
        FileOutputStream stream = new FileOutputStream(new File(filePath));
        stream.write(ttfOutputStream.toByteArray());
        stream.close();
    }

    private byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }
    private byte[] getBytes(short i) {
        System.out.println(i);
        return ByteBuffer.allocate(2).putShort(i).array();
    }
}
