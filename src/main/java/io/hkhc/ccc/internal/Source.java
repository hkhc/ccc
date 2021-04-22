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

import io.hkhc.ccc.CCCException;

/**
 * Created by herman on 8/9/15.
 */
public interface Source {

    static final String CCC_TABLE_FILE = "io/hkhc/ccc/ccc-table-01.dat";

    void load() throws CCCException; // load with default filename.
    void load(String destination) throws CCCException;

    int getCCC(int c);
    int getCodeCount();

}
