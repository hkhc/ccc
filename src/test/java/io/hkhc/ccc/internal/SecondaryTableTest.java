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

import io.hkhc.utils.ByteUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 13/12/2017.
 */

public class SecondaryTableTest {

    @Test
    public void testEmptyTable() {
        Prioritizer prioritizer = new Prioritizer();
        SecondaryTable secondaryTable = new SecondaryTable(prioritizer);

        assertThat(secondaryTable.getCodeCount()).isEqualTo(0);

        byte[] data = secondaryTable.getTable();
        assertThat(data).hasSize(2);
        assertThat(ByteUtils.getShort(data, 0)).isEqualTo(0);
    }

    @Test
    public void testSimpleTable() {
        Prioritizer prioritizer = new Prioritizer();
        SecondaryTable secondaryTable = new SecondaryTable(prioritizer);

        CharEntry entry = new CharEntry();
        entry.setUnicode(0x4567);
        entry.setCcc(0x1000);
        secondaryTable.register(entry);

        assertThat(secondaryTable.getCodeCount()).isEqualTo(1);

        byte[] data = secondaryTable.getTable();
        assertThat(data).hasSize(8);
        assertThat(ByteUtils.getShort(data, 0)).isEqualTo(1);
        assertThat(ByteUtils.getInt(data, 2)).isEqualTo(0x4567);
        assertThat(ByteUtils.getShort(data, 6)).isEqualTo(0x1000);
    }

    @Test
    public void testFillInAndLookup() throws IOException {

        Prioritizer prioritizer = new Prioritizer();
        SecondaryTable secondaryTable = new SecondaryTable(prioritizer);

        CharEntry entry = new CharEntry();
        entry.setUnicode(0x4567);
        entry.setCcc(0x1000);
        secondaryTable.register(entry);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(secondaryTable.getTable());
        baos.close();

        SecondaryTable st2 = new SecondaryTable(null);
        st2.fillIn(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));

        assertThat(st2.lookup(0x4567)).isEqualTo(0x1000);

    }

}
