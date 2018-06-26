package io.github.lingalone.javafonttools.font;

import io.github.lingalone.javafonttools.font.ttf.TTF;
import io.github.lingalone.javafonttools.font.ttf.TTFHeader;
import io.github.lingalone.javafonttools.font.ttf.TableRecord;
import io.github.lingalone.javafonttools.font.woff.TableDirectory;
import io.github.lingalone.javafonttools.font.woff.Woff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/15
 */

public class FontTransfer {


    public static TTF woffToTTF(Woff woff){
        TTF ttf = new TTF();

        int offset = 0;
        TTFHeader header = new TTFHeader();
        header.setSfntVersion(woff.getWoffHeader().getFlavor());
        header.setNumTables(woff.getWoffHeader().getNumTables());

        int numTables = woff.getWoffHeader().getNumTables();
        int temp = numTables;
        int searchRange = 16;
        short entrySelector = 0;
        while (temp > 1) {
            temp = temp >> 1;
            entrySelector++;
            searchRange = (searchRange << 1);
        }
        short rangeShift = (short) (numTables * 16 - searchRange);
        header.setSearchRange(searchRange);
        header.setEntrySelector(entrySelector);
        header.setRangeShift(rangeShift);
        ttf.setTtfHeader(header);
        offset += 12;
        ttf.setTtfHeaderOffset(offset);
        offset += 16 * numTables;
        ttf.setTableRecordsOffset(offset);


        //-------------------------------
        List<TableRecord> tableRecords = new ArrayList<>();
        List<byte[]> tableData = new ArrayList<>();
        int count = 0;
        for(TableDirectory directory : woff.getTableDirectories()){

            TableRecord tableRecord = new TableRecord();
            tableRecord.setTag(directory.getTag());
            tableRecord.setCheckSum(directory.getOrigChecksum());
            tableRecord.setLength(directory.getOrigLength());
            tableRecord.setOffset(offset);
            //
            byte [] data = woff.getTableData().get(count);

            offset += directory.getOrigLength();
            int padding = 0;
            if (offset % 4 != 0) {
                padding = 4 - (offset % 4);
                offset += padding;
            }
            tableRecord.setPadding(padding);

            tableRecords.add(tableRecord);
            tableData.add(data);
            count++;
        }
        ttf.setTableRecords(tableRecords);
        ttf.setTableData(tableData);
        //-------------------------------

        return ttf;

    }






}
