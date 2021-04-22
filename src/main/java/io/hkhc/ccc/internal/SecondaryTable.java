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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by herman on 18/12/2017.
 */
public class SecondaryTable {

    private Prioritizer prioritizer;
    private Map<Integer, Integer> secondaryMap = new HashMap<>();

    public SecondaryTable(Prioritizer prioritizer) {
        this.prioritizer = prioritizer;
    }

    public void register(CharEntry entry) {

        int priority = prioritizer.getPriority(entry.getGroup());
        if (prioritizer.shouldOverride(priority, entry.getUnicode())) {
            secondaryMap.put(entry.getUnicode(), entry.getCcc());
            prioritizer.register(entry.getUnicode(), priority);
        }

    }

    public byte[] getTable() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DataOutputStream os = new DataOutputStream(baos);
            os.writeShort(secondaryMap.size());
            for (Map.Entry<Integer, Integer> e : secondaryMap.entrySet()) {
                os.writeInt(e.getKey());
                os.writeShort(e.getValue());
            }
        } catch (IOException e) {
            // should never reach there as we are writing to ByteArrayOuptutStream
        }

        return baos.toByteArray();

    }

    public int getCodeCount() {
        return secondaryMap.size();
    }

    public void fillIn(DataInputStream dis) throws IOException {

        int secondMapSize = dis.readUnsignedShort();

        for(int i=0;i<secondMapSize;i+=2) {
            int key = dis.readInt();
            int value = dis.readUnsignedShort();
            putTo2ndMap(key, value);
        }

    }

    protected void putTo2ndMap(int c, int code) {
        if (secondaryMap.get(c)!=null) {
            return;
        }
        secondaryMap.put(c, code);
    }

    public int lookup(int c) {
        return secondaryMap.get(c);
    }

}
