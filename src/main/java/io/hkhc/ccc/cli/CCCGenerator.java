/*
 * Copyright (c) 2018. Herman Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.hkhc.ccc.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.hkhc.ccc.internal.CharEntry;
import io.hkhc.ccc.internal.Header;
import io.hkhc.ccc.internal.PrimaryTable;
import io.hkhc.ccc.internal.Prioritizer;
import io.hkhc.ccc.internal.SecondaryTable;
import io.hkhc.ccc.internal.SourceParser;
import io.hkhc.utils.StringUtils;

/**
 * Created by herman on 8/9/15.
 */
public class CCCGenerator {

    public void generate(InputStream sourceInputStream, OutputStream outputStream)
            throws IOException {

        Prioritizer prioritizer = new Prioritizer();
        Header header = new Header();

        int maxUnicode = Integer.MIN_VALUE;
        int minUnicode = Integer.MAX_VALUE;
        int startOffset = 0x4e00;
        int endOffset = 0x9fb0;
        header.setStartOffset(startOffset);
        header.setEndOffset(endOffset);

        PrimaryTable primaryTable = new PrimaryTable(header, prioritizer);
        SecondaryTable secondaryTable = new SecondaryTable(prioritizer);

        SourceParser parser = new SourceParser(prioritizer, sourceInputStream);

        StringUtils.IdeographicChecker ideographicChecker = StringUtils.getIdeographicChecker();

        CharEntry entry;
        while ((entry=parser.getNextEntry())!=null) {
            int unicode = entry.getUnicode();
            if (ideographicChecker.isIdeographic(unicode) ) {
                if (unicode>=startOffset && unicode<=endOffset) {
                    if (unicode>maxUnicode) maxUnicode = unicode;
                    if (unicode<minUnicode) minUnicode = unicode;
                    primaryTable.register(entry);

                }
                else {
                    System.out.println("2nd map " + Integer.toHexString(entry.getUnicode()).toUpperCase() + " ("+(char)entry.getUnicode()+")");
                    secondaryTable.register(entry);
                }
            }
            else {
                System.out.println("not ideographic ["+Integer.toHexString(unicode)+","+(char)unicode+"]");
            }
        }

        System.out.println("max unicode = " + maxUnicode);
        System.out.println("min unicode = " + minUnicode);
        System.out.println("code count = " + primaryTable.getCodeCount());
        System.out.println("code2 count = " + secondaryTable.getCodeCount());

        byte[] secondBuffer = secondaryTable.getTable();
        header.updateChecksum(primaryTable.getTable(), secondBuffer);

        outputStream.write(header.generate());
        outputStream.write(primaryTable.getTable());
        outputStream.write(secondBuffer);
        outputStream.close();

    }

    public static void main(String[] args) throws Exception {


        FileInputStream is = new FileInputStream("data/io.hkhc.ccc-source-v2.txt");
        FileOutputStream fos = new FileOutputStream("data/io.hkhc.ccc-table-01.dat");

        CCCGenerator generator = new CCCGenerator();
        generator.generate(is, fos);


    }

}
