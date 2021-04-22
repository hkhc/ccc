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

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 7/12/2017.
 */

public class CCCDBValidatorTest {

    byte twoComp(int b) {
        return (byte)(-~b-1);
    }

    @Test
    public void testGetInt() {

        CCCDBValidator validator = new CCCDBValidator();

        assertThat(validator.getInt(new byte[] { 0x12, 0x34, 0x56, 0x78}, 0))
                .isEqualTo(0x12345678);

        assertThat(validator.getInt(new byte[] { twoComp(0x87), 0x65, 0x43, 0x21}, 0))
                .isEqualTo(0x87654321);

        assertThat(validator.getInt(new byte[] { 0x00, 0x00, 0x12, 0x34, 0x56, 0x78}, 2))
                .isEqualTo(0x12345678);

        assertThat(validator.getShort(new byte[] { 0x12, 0x34}, 0))
                .isEqualTo(0x1234);

        assertThat(Integer.toHexString(validator.getShort(new byte[] { twoComp(0x87), 0x65}, 0)))
                .isEqualTo(Integer.toHexString(0x8765));

        assertThat(Integer.toHexString(validator.getShort(new byte[] { twoComp(0x9f), twoComp(0xb0)}, 0)))
                .isEqualTo(Integer.toHexString(0x9fb0));

        assertThat(validator.getShort(new byte[] { 0x11, 0x11, 0x12, 0x34}, 2))
                .isEqualTo(0x1234);

    }

    @Test
    // ensure the format of splitting short into bytes by bit shifting is compatable with
    // DataInputStream.readShort
    public void testReadShort() throws IOException {

        byte[] data = new byte[2];
        for(int i=0;i<65536;i++) {
            data[0] = (byte)((i >> 8) & 0xff);
            data[1] = (byte)(i & 0xff);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            int s = dis.readShort();
            if (s<0) s+=65536;
//            if (s<0) s = (~s+1);
            assertThat(s).isEqualTo(i);
        }

    }

}
