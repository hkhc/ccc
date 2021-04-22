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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by herman on 12/12/2017.
 */

public class PrioritizerTest {

    @Test
    public void testIsGroup() {

        Prioritizer p = new Prioritizer();

        assertThat(p.isGroup("g_old")).isTrue();
        assertThat(p.isGroup("xxx")).isFalse();

    }

    @Test
    public void testGetPriority() {

        Prioritizer p = new Prioritizer();

        assertThat(p.getPriority("g_old")).isEqualTo(0);
        assertThat(p.getPriority("g_china1")).isEqualTo(4);
        assertThat(p.getPriority("g_china2")).isEqualTo(5);

    }

    @Test
    public void testShouldOverride() {

        Prioritizer p = new Prioritizer();

        // the first call always give true as there is nothing in char map
        assertThat(p.shouldOverride(4, 0x5555)).isTrue();

        p.register(4, 0x5555);

        assertThat(p.shouldOverride(3, 0x5555)).isTrue();
        assertThat(p.shouldOverride(5, 0x5555)).isFalse();

    }

    @Test
    public void testRegister() {

        Prioritizer p = new Prioritizer();

        assertThat(p.register(10, 1234)).isTrue();

        // same unicode add again, no new item is added
        assertThat(p.register(5, 1234)).isFalse();

        // different unicode, new item is added
        assertThat(p.register(5, 2345)).isTrue();

    }

}
