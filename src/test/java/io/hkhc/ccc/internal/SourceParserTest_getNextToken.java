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

import io.hkhc.ccc.internal.SourceParser.Token;

import static io.hkhc.ccc.internal.SourceParser.TokenType.ccc;
import static io.hkhc.ccc.internal.SourceParser.TokenType.group;
import static io.hkhc.ccc.internal.SourceParser.TokenType.unicode;
import static io.hkhc.ccc.internal.SourceParser.TokenType.unknown;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 16/12/2017.
 */

public class SourceParserTest_getNextToken {

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
    public void test_empty_data() throws IOException {

        parser = newParser("");
        assertThat(parser.getNextToken()).isNull();

    }

    @Test
    public void test_common_one_entry_line() throws IOException {

        parser = newParser("1234 g_s1 U+2345 XXX");
        assertThat(parser.getNextToken()).isEqualTo(new Token(ccc, "1234"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(group, "g_s1"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+2345"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "XXX"));
        assertThat(parser.getNextToken()).isNull();

    }


    @Test
    public void test_common_two_entry_line() throws IOException {

        parser = newParser("1234 g_s1 U+2345 XXX g_s2 U+4567 YYY");
        assertThat(parser.getNextToken()).isEqualTo(new Token(ccc, "1234"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(group, "g_s1"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+2345"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "XXX"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(group, "g_s2"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+4567"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "YYY"));
        assertThat(parser.getNextToken()).isNull();

    }

    @Test
    public void test_one_line_without_group() throws IOException {

        parser = newParser("1234 U+2345 XXX ");
        assertThat(parser.getNextToken()).isEqualTo(new Token(ccc, "1234"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+2345"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "XXX"));
        assertThat(parser.getNextToken()).isNull();

    }

    @Test
    public void test_two_lines() throws IOException {

        parser = newParser(
                "1234 U+2345 XXX \n"+
                    "1235 g_s1 U+3456 YYY");
        assertThat(parser.getNextToken()).isEqualTo(new Token(ccc, "1234"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+2345"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "XXX"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(ccc, "1235"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(group, "g_s1"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unicode, "U+3456"));
        assertThat(parser.getNextToken()).isEqualTo(new Token(unknown, "YYY"));
        assertThat(parser.getNextToken()).isNull();

    }

}
