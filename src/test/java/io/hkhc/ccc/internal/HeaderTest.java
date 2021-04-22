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

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 9/12/2017.
 */

public class HeaderTest {

    @Test(expected = IllegalStateException.class)
    public void testIncompleteData1() {

        Header header = new Header();
        header.generate();

    }

    @Test(expected = IllegalStateException.class)
    public void testIncompleteData2() {

        Header header = new Header();
        header.setStartOffset(0);
        header.generate();

    }

    @Test(expected = IllegalStateException.class)
    public void testIncompleteData3() {

        Header header = new Header();
        header.setStartOffset(0);
        assertThat(header.generate()).isEqualTo(
                new byte[] {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00});

    }

    @Test
    public void testEmptyHeader() {

        Header header = new Header();
        header.setStartOffset(0);
        header.setEndOffset(0);
        header.updateChecksum(new byte[] {}, new byte[] {});
        assertThat(header.generate()).isEqualTo(
                new byte[] {0x00,0x04,0x00,0x01,0x00,0x00,0x00,0x00});

        header.setStartOffset(10);
        header.setEndOffset(10);
        header.updateChecksum(new byte[] {}, new byte[] {});
        assertThat(header.generate()).isEqualTo(
                new byte[] {0x00,0x2c,0x00,0x15,0x00,0x0a,0x00,0x0a});

        header.setStartOffset(10);
        header.setEndOffset(12);
        header.updateChecksum(new byte[] {0x12, 0x34, 0x56, 0x78}, new byte[] {});
        assertThat(header.generate()).isEqualTo(
                new byte[] {0x02,-0x6e,0x01,0x2b,0x00,0x0a,0x00,0x0c});

        header.setStartOffset(10);
        header.setEndOffset(12);
        header.updateChecksum(new byte[] {0x12, 0x34, 0x56, 0x78},
                new byte[] {0x00, 0x01, 0x12, 0x34, 0x56, 0x78});
        assertThat(header.generate()).isEqualTo(
                new byte[] {0x0b,-0x5f,0x02,0x40,0x00,0x0a,0x00,0x0c});

    }

    @Test
    public void testFillIn() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(0x12345678);
        dos.writeShort(0x8899);
        dos.writeShort(0x1234);
        dos.close();

        Header header = new Header();
        header.fillIn(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));

        assertThat(header.getChecksum()).isEqualTo(0x12345678);
        assertThat(header.getStartOffset()).isEqualTo(0x8899);
        assertThat(header.getEndOffset()).isEqualTo(0x1234);

    }

}
