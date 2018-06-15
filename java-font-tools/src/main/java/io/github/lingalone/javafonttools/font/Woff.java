package io.github.lingalone.javafonttools.font;


import io.github.lingalone.javafonttools.font.woff.TableDirectory;
import io.github.lingalone.javafonttools.font.woff.WoffHeader;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link https://www.w3.org/TR/2012/REC-WOFF-20121213/
 * @link https://docs.microsoft.com/zh-cn/typography/opentype/spec/font-file
 * @since 2018/6/15
 */

@Data
public class Woff {

    private WoffHeader              woffHeader;
    private int                     woffHeaderOffset;
    private List<TableDirectory>    tableDirectories;
    private List<byte[]>            tableData;
    private int                     tableDirectoriesOffset;
    private int                     tableNum;
    private Map<Integer, String>    glyphCode;
    private byte []                 fontFile;


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
