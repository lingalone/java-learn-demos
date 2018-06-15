package io.github.lingalone.javafonttools.font.woff;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The header includes an identifying signature and indicates the specific kind of font data
 * included in the file (TrueType or CFF outline data); it also has a font version number, offsets to additional data chunks,
 * and the number of entries in the table directory that immediately follows the header:
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */

@Data
public class WoffHeader {

    /*
        UInt32	signature	    0x774F4646 'wOFF'
        UInt32	flavor	        The "sfnt version" of the input font.
        UInt32	length	        Total size of the WOFF file.
        UInt16	numTables	    Number of entries in directory of font tables.
        UInt16	reserved	    Reserved; set to zero.
        UInt32	totalSfntSize	Total size needed for the uncompressed font data, including the sfnt header, directory, and font tables (including padding).
        UInt16	majorVersion	Major version of the WOFF file.
        UInt16	minorVersion	Minor version of the WOFF file.
        UInt32	metaOffset	    Offset to metadata block, from beginning of WOFF file.
        UInt32	metaLength	    Length of compressed metadata block.
        UInt32	metaOrigLength	Uncompressed size of metadata block.
        UInt32	privOffset	    Offset to private data block, from beginning of WOFF file.
        UInt32	privLength	    Length of private data block.
    */

    private int     signature;
    private int     flavor;
    private int     length;
    private int     numTables;
    private int     reserved;
    private int     totalSfntSize;
    private int     majorVersion;
    private int     minorVersion;
    private int     metaOffset;
    private int     metaLength;
    private int     metaOrigLength;
    private int     privOffset;
    private int     privLength;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
