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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 16/12/2017.
 */

public class SourceParserTest_handleNextToken {

    Prioritizer prioritizer;
    SourceParser parser;

    @Before
    public void setUp() throws IOException {
        prioritizer = new Prioritizer();
        parser = new SourceParser(prioritizer,new ByteArrayInputStream("hello".getBytes()));
    }

    @Test
    public void test_with_group_token() throws IOException {

        Token token = new Token(ccc, "1234");

        assertThat(parser.handleNextToken(
                new Token(ccc, "1234"))).isFalse();
        assertThat(parser.getCurrCode()).isEqualTo(1234);
        assertThat(parser.getCurrEntry()).isNull();

        assertThat(parser.handleNextToken(
                new Token(group, "g_s1"))).isFalse();
        assertThat(parser.getCurrEntry().getCcc()).isEqualTo(1234);
        assertThat(parser.getCurrEntry().getGroup()).isEqualTo("g_s1");

        assertThat(parser.handleNextToken(
                new Token(unicode, "U+2345"))).isTrue();
        assertThat(parser.getCurrEntry().getCcc()).isEqualTo(1234);
        assertThat(parser.getCurrEntry().getGroup()).isEqualTo("g_s1");
        assertThat(parser.getCurrEntry().getUnicode()).isEqualTo(0x2345);

    }

    @Test
    public void test_without_group_token() throws IOException {

        Token token = new Token(ccc, "1234");

        assertThat(parser.handleNextToken(
                new Token(ccc, "1234"))).isFalse();
        assertThat(parser.getCurrCode()).isEqualTo(1234);
        assertThat(parser.getCurrEntry()).isNull();

        assertThat(parser.handleNextToken(
                new Token(unicode, "U+2345"))).isTrue();
        assertThat(parser.getCurrEntry().getCcc()).isEqualTo(1234);
        assertThat(parser.getCurrEntry().getGroup()).isNull();
        assertThat(parser.getCurrEntry().getUnicode()).isEqualTo(0x2345);

    }


}
