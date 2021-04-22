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

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 16/12/2017.
 */

public class SourceParserTest_getNextEntry {

    Prioritizer prioritizer;
    SourceParser parser;

    @Before
    public void setUp() {
        prioritizer = new Prioritizer();
    }

    private SourceParser newParser(String data) throws IOException {
        return new SourceParser(prioritizer, new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    public void test_one_entry() throws IOException {

        parser = newParser("1234 g_s1 U+2345 XXX");

        CharEntry entry = parser.getNextEntry();
        assertThat(entry.getCcc()).isEqualTo(1234);
        assertThat(entry.getGroup()).isEqualTo("g_s1");
        assertThat(entry.getUnicode()).isEqualTo(0x2345);

        assertThat(parser.getCurrEntry()).isNull();

    }

    @Test
    public void test_two_entries_in_one_line() throws IOException {

        parser = newParser("1234 g_s1 U+2345 XXX g_s2 U+2346 YYY");

        CharEntry entry = parser.getNextEntry();
        assertThat(entry.getCcc()).isEqualTo(1234);
        assertThat(entry.getGroup()).isEqualTo("g_s1");
        assertThat(entry.getUnicode()).isEqualTo(0x2345);

        entry = parser.getNextEntry();
        assertThat(entry.getCcc()).isEqualTo(1234);
        assertThat(entry.getGroup()).isEqualTo("g_s2");
        assertThat(entry.getUnicode()).isEqualTo(0x2346);

        assertThat(parser.getCurrEntry()).isNull();

    }

    @Test
    public void test_two_entries_in_two_lines() throws IOException {

        parser = newParser("1234 g_s1 U+2345 XXX\n1235 g_s2 U+2346 YYY");

        CharEntry entry = parser.getNextEntry();
        assertThat(entry.getCcc()).isEqualTo(1234);
        assertThat(entry.getGroup()).isEqualTo("g_s1");
        assertThat(entry.getUnicode()).isEqualTo(0x2345);

        entry = parser.getNextEntry();
        assertThat(entry.getCcc()).isEqualTo(1235);
        assertThat(entry.getGroup()).isEqualTo("g_s2");
        assertThat(entry.getUnicode()).isEqualTo(0x2346);

        assertThat(parser.getCurrEntry()).isNull();

    }

}
