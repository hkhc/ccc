/*
 * Copyright (c) 2021. Herman Cheung
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
 */

package io.hkhc.ccc.cli;

import java.io.FileInputStream;
import java.io.IOException;

import io.hkhc.utils.FileUtils;

/**
 * Created by herman on 7/12/2017.
 */

public class CCCDBValidator {

    public static void main(String[] args) throws Exception {

        String filename = "data/io.hkhc.ccc-table-01.dat";
        CCCDBValidator validator = new CCCDBValidator();
        validator.validate(filename);

    }

    public void validate(String filename) throws IOException {

        byte[] data = FileUtils.readStreamToByteArray(new FileInputStream(filename));

        int offset = 0;
        int checksum = getInt(data, offset);
        offset+=4;
        int startOffset = toPosInt(getShort(data, offset));
        System.out.println("start offset = " + startOffset);
        offset+=2;
        int endOffset = toPosInt(getShort(data, offset));
        System.out.println("end offset = " + endOffset);
        offset+=2;
        offset+=(endOffset-startOffset)*2;
        int secondMapSize = toPosInt(getShort(data, offset));
        System.out.println("second map size = " + secondMapSize);
        if (secondMapSize>0) {
            offset += 2;
            int firstEntryKey = toPosInt(getShort(data, offset));
            offset += 2;
            int firstEntryValue = toPosInt(getShort(data, offset));
            offset+=2;
            System.out.println("First entry in second map : " + Integer.toHexString(firstEntryKey) + " "
                    + Integer.toHexString(firstEntryValue));
            int secondEntryKey = toPosInt(getShort(data, offset));
            offset+=2;
            int secondEntryValue = toPosInt(getShort(data, offset));
            offset+=2;
            System.out.println("Second entry in second map : " + Integer.toHexString(secondEntryKey) + " "
                    + Integer.toHexString(secondEntryValue));
        }


        System.out.println("checksum = " + Integer.toHexString(checksum));
        System.out.println("start offset = " + Integer.toHexString(startOffset));
        System.out.println("end offset = " + Integer.toHexString(endOffset));
        System.out.println("2nd map size = " + Integer.toHexString(secondMapSize));

    }

    int getShort(byte[] data, int start) {

        int result = 0;
        result = result | (data[start] & 0xff);
        result <<= 8;
        result = result | (data[start+1] & 0xff);

        result &= 0xffff;
        System.out.println("getShort = " + result);

        return result;

    }

    int getInt(byte[] data, int start) {

        int result = 0;
        result = result | (data[start] & 0xff);
        result <<= 8;
        result = result | (data[start+1] & 0xff);
        result <<= 8;
        result = result | (data[start+2] & 0xff);
        result <<= 8;
        result = result | (data[start+3] & 0xff);

        result &= 0xffffffff;

        return result;

    }

    int toPosInt(int s) {
        if (s<0)
            return s+65536;
        else
            return s;
    }

}
