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
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 12/12/2017.
 */

public class PrimaryTableTest {

    @Test
    public void testOne() {
        Header header = new Header();
        header.setStartOffset(0x5555);
        header.setEndOffset(0x5556);
        Prioritizer prioritizer = new Prioritizer();
        PrimaryTable pt = new PrimaryTable(header, prioritizer);

        assertThat(pt.getCodeCount()).isEqualTo(0);
        assertThat(pt.getTable().length).isEqualTo(2*(header.getEndOffset()-header.getStartOffset()));
        assertThat(pt.getTable()).isEqualTo(new byte[] {0x00, 0x00});
        assertThat(pt.getCodeCount()).isEqualTo(0);


        CharEntry entry = new CharEntry();
        entry.setUnicode(0x5555);
        entry.setCcc(0x1234);
        entry.setGroup("g_s1");
        pt.register(entry);

        assertThat(pt.getTable().length).isEqualTo(2*(header.getEndOffset()-header.getStartOffset()));
        assertThat(pt.getTable()).isEqualTo(new byte[] {0x12, 0x34});
        assertThat(pt.getCodeCount()).isEqualTo(1);

        entry = new CharEntry();
        entry.setUnicode(0x5555);
        entry.setCcc(0x2345);
        entry.setGroup("g_s2");
        pt.register(entry);

        // table remain unchanged because the group priority of second "register" call is lower
        assertThat(pt.getTable().length).isEqualTo(2*(header.getEndOffset()-header.getStartOffset()));
        assertThat(pt.getTable()).isEqualTo(new byte[] {0x12, 0x34});
        assertThat(pt.getCodeCount()).isEqualTo(1);

        entry = new CharEntry();
        entry.setUnicode(0x5555);
        entry.setCcc(0x2345);
        entry.setGroup("g_old");
        pt.register(entry);

        // table changed because the group priority of second "register" call is higher
        assertThat(pt.getTable().length).isEqualTo(2*(header.getEndOffset()-header.getStartOffset()));
        assertThat(pt.getTable()).isEqualTo(new byte[] {0x23, 0x45});
        assertThat(pt.getCodeCount()).isEqualTo(1);

    }

    @Test
    public void testTwo() {
        Header header = new Header();
        header.setStartOffset(0x5555);
        header.setEndOffset(0x5557);
        Prioritizer prioritizer = new Prioritizer();
        PrimaryTable pt = new PrimaryTable(header, prioritizer);

        assertThat(pt.getCodeCount()).isEqualTo(0);
        assertThat(pt.getTable().length).isEqualTo(2 * (header.getEndOffset() - header.getStartOffset()));
        assertThat(pt.getTable()).isEqualTo(new byte[]{0x00, 0x00, 0x00, 0x00});
        assertThat(pt.getCodeCount()).isEqualTo(0);

        CharEntry entry = new CharEntry();
        entry.setUnicode(0x5555);
        entry.setCcc(0x1234);
        entry.setGroup("g_s1");
        pt.register(entry);

        entry = new CharEntry();
        entry.setUnicode(0x5556);
        entry.setCcc(0x2345);
        entry.setGroup("g_s1");
        pt.register(entry);

        assertThat(pt.getTable()).isEqualTo(new byte[]{0x12, 0x34, 0x23, 0x45});
        assertThat(pt.getCodeCount()).isEqualTo(2);

    }

    @Test
    public void testFillInAndLookup() throws IOException {

        Header header = new Header();
        header.setStartOffset(0x5555);
        header.setEndOffset(0x5557);
        Prioritizer prioritizer = new Prioritizer();
        PrimaryTable pt = new PrimaryTable(header, prioritizer);

        CharEntry entry = new CharEntry();
        entry.setCcc(1234);
        entry.setUnicode(0x5555);

        pt.register(entry);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(pt.getTable());
        baos.close();

        PrimaryTable pt2 = new PrimaryTable(header, null);
        pt2.fiilIn(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));

        assertThat(pt2.lookup(0x5555)).isEqualTo(1234);


    }

}
