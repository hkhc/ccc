/*
 * Copyright (c) 2021. Herman Cheung
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
 */

package io.hkhc.ccc;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.hkhc.ccc.internal.Header;
import io.hkhc.ccc.internal.PrimaryTable;
import io.hkhc.ccc.internal.SecondaryTable;
import io.hkhc.ccc.internal.Source;
import io.hkhc.log.IHLog;
import io.hkhc.log.Logger;

/**
 * Created by herman on 8/9/15.
 */
public abstract class BaseSource implements Source {

    private static final String TAG = Consts.logTag("BS");
    private static final IHLog l = Logger.getL(BaseSource.class);

    private PrimaryTable primaryTable = null;
    private SecondaryTable secondaryTable = null;

    public void load(@SuppressWarnings("SameParameterValue") String filename) throws CCCException {
        try {
            load(new FileInputStream(filename));
        }
        catch (FileNotFoundException e) {
            throw new CCCException(e);
        }
    }

    public void load(InputStream is) throws CCCException {

        try {

            DataInputStream dis = new DataInputStream(is);

            // init header

            Header header = new Header();
            header.fillIn(dis);


            // init 1st map

            primaryTable = new PrimaryTable(header, null);
            primaryTable.fiilIn(dis);

            // init 2nd map

            secondaryTable = new SecondaryTable(null);
            secondaryTable.fillIn(dis);

        }
        catch (EOFException e) {
            l.err("EOFException", e);
            throw new CCCException("Failed to load character map", e);
        }
        catch (IOException e) {
            l.err("IOException", e);
        }
        finally{
            if (is!=null) try { is.close(); } catch (IOException e) {}
            l.err("finish load");
        }

    }

    @Override
    public int getCCC(int c) {
        int ccc = primaryTable.lookup(c);
        if (ccc!=0) return ccc;
        return secondaryTable.lookup(c);
    }

    @Override
    public int getCodeCount() {

        return primaryTable.getCodeCount() + secondaryTable.getCodeCount();

    }
}
