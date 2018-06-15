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
public class TableRecord {

    /*
        uint32	    tag	4       -byte identifier.
        uint32	    checkSum	CheckSum for this table.
        Offset32	offset	    Offset from beginning of TrueType font file.
        uint32	    length	    Length of this table.
    */
    private int tag;
    private int checkSum;
    private int offset;
    private int length;
    private int padding;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
