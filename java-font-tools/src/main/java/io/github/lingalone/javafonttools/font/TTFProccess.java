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
        ByteArrayOutputStream ttfOutputStream = new ByteArrayOutputStream();
        ttfOutputStream.write(getBytes((Integer) ttf.getTtfHeader().getSfntVersion()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getNumTables()));
        ttfOutputStream.write(getBytes((short) ttf.getTtfHeader().getSearchRange()));
        ttfOutputStream.write(getBytes( ttf.getTtfHeader().getEntrySelector()));
        ttfOutputStream.write(getBytes(ttf.getTtfHeader().getRangeShift()));

        for (TableRecord tableRecord : ttf.getTableRecords()) {
            ttfOutputStream.write(getBytes((Integer) tableRecord.getTag()));
            ttfOutputStream.write(getBytes((Integer) tableRecord.getCheckSum()));
            ttfOutputStream.write(getBytes((Integer) tableRecord.getOffset()));
            ttfOutputStream.write(getBytes((Integer) tableRecord.getLength()));
        }

        int count = 0;
        for (byte[] data : ttf.getTableData()){
            ttfOutputStream.write(data);
            ttfOutputStream.write(getBytes(0), 0, ttf.getTableRecords().get(count).getPadding());
        }

        byte[] ttfByteArray = ttfOutputStream.toByteArray();
        FileOutputStream stream = new FileOutputStream(new File(filePath));
        stream.write(ttfByteArray);
        stream.close();
    }

    private byte[] getBytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }
}
