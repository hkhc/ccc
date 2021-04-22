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
import java.io.IOException;

/**
 * Created by herman on 16/12/2017.
 */

public class SourceParserTest {

    @Test(expected=NullPointerException.class)
    public void test_null_prioritizer() throws IOException {
        SourceParser parser = new SourceParser(null, new ByteArrayInputStream(new byte[] {}));
    }

    @Test(expected=NullPointerException.class)
    public void test_null_inputstream() throws IOException {
        SourceParser parser = new SourceParser(new Prioritizer(), null);
    }


}
