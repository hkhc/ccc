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

package io.hkhc.ccc.internal;

import java.io.DataInputStream;
import java.io.IOException;

import io.hkhc.utils.ByteUtils;

/**
 * Created by herman on 18/12/2017.
 */
public class PrimaryTable {
    private byte[] table;
    private Header header;
    private Prioritizer prioritizer;
    private int codeCount = 0;

    public PrimaryTable(Header header, Prioritizer prioritizer) {
        this.header = header;
        this.prioritizer = prioritizer;
        table = new byte[(header.getEndOffset() - header.getStartOffset()) * 2];
    }

    public void register(CharEntry entry) {

        int priority = prioritizer.getPriority(entry.getGroup());

        if (prioritizer.shouldOverride(priority, entry.getUnicode())) {
            int offset = (entry.getUnicode() - header.getStartOffset()) * 2;
            ByteUtils.fillShort(table, offset, entry.getCcc());
            boolean newItem = prioritizer.register(priority, entry.getUnicode());
            if (newItem) codeCount++;
        }
    }

    public int getCodeCount() {
        return codeCount;
    }

    public byte[] getTable() {
        return table;
    }

    public void fiilIn(DataInputStream dis) throws IOException {
        table = new byte[(header.getEndOffset()-header.getStartOffset())*2];
        dis.readFully(table);
    }

    public int lookup(int unicode) {
        int unicodeOffset = unicode - header.getStartOffset();
        if (unicodeOffset<0) return 0;
        if (unicodeOffset>=table.length) return 0;
        return ByteUtils.getShort(table,unicodeOffset*2);
    }

//    public int getFrom1stMap(int c) {
//
//        ShortBuffer codeBuffer = getCodeBuffer();
//        int code = c-getStartOffset();
////        int startOffset = getStartOffset();
//
//        if (codeBuffer==null) return 0;
//
//        if (code<0) return 0;
//        if (code> codeBuffer.limit()) return 0;
//
//        return codeBuffer.get(code);
//    }


}
