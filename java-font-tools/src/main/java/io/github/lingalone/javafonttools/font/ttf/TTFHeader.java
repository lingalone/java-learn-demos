package io.github.lingalone.javafonttools.font.ttf;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */
@Data
public class TTFHeader {

    /*
        uint32	sfntVersion	0x00010000 or 0x4F54544F ('OTTO') — see below.
        uint16	numTables	Number of tables.
        uint16	searchRange	(Maximum power of 2 <= numTables) x 16.
        uint16	entrySelector	Log2(maximum power of 2 <= numTables).
        uint16	rangeShift	NumTables x 16-searchRange.
    */

    private int sfntVersion;
    private int numTables;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
