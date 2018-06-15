package io.github.lingalone.javafonttools.font.woff;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The table directory is an array of WOFF table directory entries, as defined below.
 * The directory follows immediately after the WOFF file header;
 * therefore, there is no explicit offset in the header pointing to this block.
 * Its size is calculated by multiplying the numTables value in the WOFF header times the size of a single WOFF table directory.
 * Each table directory entry specifies the size and location of a single font data table.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */

@Data
public class TableDirectory {

    /*
        UInt32	tag	            4-byte sfnt table identifier.
        UInt32	offset	        Offset to the data, from beginning of WOFF file.
        UInt32	compLength	    Length of the compressed data, excluding padding.
        UInt32	origLength	    Length of the uncompressed table, excluding padding.
        UInt32	origChecksum	Checksum of the uncompressed table.
    */

    private int tag;
    private int offset;
    private int compLength;
    private int origLength;
    private int origChecksum;
    private byte[] data;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
