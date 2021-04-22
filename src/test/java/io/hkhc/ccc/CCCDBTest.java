/*
 * Copyright (c) 2017. Herman Cheung
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

package io.hkhc.ccc;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by herman on 13/8/15.
 */
public class CCCDBTest {

    CCCDB db = null;

    @Before
    public void before() {
//        db.load(CCCDB.getDefaultFilename());
    }

    @Test
    public void testAndroidMemSource() throws CCCException {
        db = new CCCDB(new ResourceSource());
        db.load();

        Assert.assertEquals(7115,db.getCCC('陳'));
        Assert.assertEquals("7115 1129 2429", db.getCCCs("陳大文"));
    }

    // failed to lookup 鄧瑞華？
    @Test
    public void testTangShuiWah() throws CCCException  {
        db = new CCCDB(new ResourceSource());
        db.load();
        Assert.assertEquals("6772 3843 5478", db.getCCCs("鄧瑞華"));
    }


}
