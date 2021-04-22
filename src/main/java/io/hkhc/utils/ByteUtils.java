/*
 * Copyright (c) 2017. Herman Cheung
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

package io.hkhc.utils;

/**
 * Helper methods for byte level operations
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ByteUtils {

    public static void fillInt(byte[] b, int offset, long value) {
        b[offset] = (byte)((value >> 24) &0xff);
        b[offset+1] = (byte)((value >> 16) &0xff);
        b[offset+2] = (byte)((value >> 8) &0xff);
        b[offset+3] = (byte)(value &0xff);
    }

    public static void fillShort(byte[] b, int offset, int value) {
        b[offset] = (byte)((value >> 8) &0xff);
        b[offset+1] = (byte)(value &0xff);
    }

    public static int getShort(byte[] data, int start) {

        int result = 0;
        result = result | (data[start] & 0xff);
        result <<= 8;
        result = result | (data[start+1] & 0xff);

        result &= 0xffff;

        return result;

    }

    public static int getInt(byte[] data, int start) {

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


}
