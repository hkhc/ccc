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
import java.util.zip.Adler32;

import io.hkhc.utils.ByteUtils;

/**
 * Created by herman on 18/12/2017.
 */
public class Header {
    private long checksum = -1;
    private int startOffset = -1;
    private int endOffset = -1;
    private byte[] data = new byte[8];

    boolean isDataComplete() {
        return checksum != -1 && startOffset != -1 && endOffset != -1;
    }

    public byte[] generate() {
        if (!isDataComplete()) {
            throw new IllegalStateException("data is not completed");
        }
        return data;
    }

    public void fillIn(DataInputStream dis) throws IOException {
        checksum = dis.readInt();
        startOffset = dis.readUnsignedShort();
        endOffset = dis.readUnsignedShort();
    }

    public void updateChecksum(byte[] section1, byte[] section2) {

        Adler32 a32 = new Adler32();
        a32.update(data, 4, data.length - 4);
        a32.update(section1);
        a32.update(section2);
        checksum = a32.getValue();
        System.out.println("checksum = " + Long.toHexString(checksum));
        ByteUtils.fillInt(data, 0, checksum);

    }

    public long getChecksum() {
        return checksum;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
        ByteUtils.fillShort(data, 4, startOffset);
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
        ByteUtils.fillShort(data, 6, endOffset);
    }
}
