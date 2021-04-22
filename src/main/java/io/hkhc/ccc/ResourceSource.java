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

package io.hkhc.ccc;

import io.hkhc.ccc.internal.Source;

/**
 * Created by herman on 8/9/15.
 */
public class ResourceSource extends BaseSource implements Source {

    private static final String TAG = Consts.logTag("AMS");

    @Override
    public void load() throws CCCException {
        load(getClass().getClassLoader().getResourceAsStream(Source.CCC_TABLE_FILE));
    }

}
