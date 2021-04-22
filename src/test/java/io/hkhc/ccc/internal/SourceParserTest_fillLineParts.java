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
import java.util.List;

import io.hkhc.ccc.SizeThrottledInputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 16/12/2017.
 */

public class SourceParserTest_fillLineParts {

    Prioritizer prioritizer;
    SourceParser parser;

    @Before
    public void setUp() {
        prioritizer = new Prioritizer();
    }

    private SourceParser newParser(String data) throws IOException {
        // set a small read size to ensure underlying IO operations can handle that.
        return new SourceParser(prioritizer,
                new SizeThrottledInputStream(new ByteArrayInputStream(data.getBytes()),8));
    }

    @Test
    /*
    Empty data shall five null result
     */
    public void test_empty_data() throws IOException {

        parser = newParser("");

        assertThat(parser.fillLineParts()).isNull();

    }

    @Test
    /*
    Single line data is read successfully
     */
    public void test_single_line() throws IOException {

        parser = newParser("a b c");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

    }

    @Test
    /*
    Single line data is read successfully
     */
    public void test_single_line_delimited_by_tab() throws IOException {

        parser = newParser("a\tb\tc");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

    }

    @Test
    /*
    Single line data and invoke fillLineParts twice, the second attempt shall return false
     */
    public void test_single_line_invoke_twice() throws IOException {

        parser = newParser("a b c");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

        assertThat(parser.fillLineParts()).isFalse();
        // parts remain unchanged
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

    }

    @Test
    /*
    Single line data and invoke fillLineParts twice, the second attempt shall return false
    after one of the parts is consumed
     */
    public void test_single_line_invoke_twice_after_consumption() throws IOException {

        parser = newParser("a b c");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

        parser.getCurrLineParts().remove(0); // 2 items remained
        assertThat(parser.fillLineParts()).isFalse();
        // reflect the latest status of the parts
        assertThat(parser.getCurrLineParts()).containsExactly("b", "c");

    }

    @Test
    /*
    Single line data and invoke fillLineParts twice, the second attempt shall return null
    after all parts are consumed
     */
    public void test_single_line_invoke_twice_after_exhaused() throws IOException {

        parser = newParser("a b c");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

        parser.getCurrLineParts().clear();
        assertThat(parser.fillLineParts()).isNull();
        assertThat(parser.getCurrLineParts()).isNull();

    }

    @Test
    /*
    Two lines of data with empty first line
     */
    public void test_empty_line_ignored() throws IOException {

        parser = newParser("\na b c");

        List<String> parts;
        assertThat(parser.fillLineParts()).isTrue();
        assertThat((parts=parser.getCurrLineParts())).containsExactly("a", "b", "c");

    }

    @Test
    /*
    Two lines of data and invoke fillLineParts twice, the second attempt shall return true
     */
    public void test_two_lines_invoke_twice_after_exhaused() throws IOException {

        parser = newParser(
                "a b c\n" +
                "d e f");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

        parser.getCurrLineParts().clear();
        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("d", "e", "f");

    }

    @Test
    /*
    Three lines of data and invoke fillLineParts twice, with second line be empty
    The second attempt shall return true
     */
    public void test_three_lines_second_line_empty() throws IOException {

        parser = newParser(
                "a b c\n\n" +
                        "d e f");

        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("a", "b", "c");

        parser.getCurrLineParts().clear();
        assertThat(parser.fillLineParts()).isTrue();
        assertThat(parser.getCurrLineParts()).containsExactly("d", "e", "f");

    }

}
