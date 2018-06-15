package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.ttf.TTFHeader;
import io.github.lingalone.javafonttools.font.ttf.TableRecord;
import lombok.Data;

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
@Data
public class TTF {

    TTFHeader                       ttfHeader;
    private int                     ttfHeaderOffset;
    List<TableRecord>               tableRecords;
    private int                     tableRecordsOffset;
    private List<byte[]>            tableData;
    private int                     tableNum;
    private Map<Integer, String>    glyphCode;
    private byte []                 fontFile;
}
