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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by herman on 18/12/2017.
 */
public class Prioritizer {

    private Map<String, Integer> priorityMap = new HashMap<>();
    private Map<Integer, Integer> charPriorityMap = new HashMap<>();

    public Prioritizer() {
        initPriorityMap();
    }

    private void initPriorityMap() {
        priorityMap.put("g_old", 0);
        priorityMap.put("g_s1", 1);
        priorityMap.put("g_s2", 2);
        priorityMap.put("g_s3", 3);
        priorityMap.put("g_china1", 4);
        priorityMap.put("g_china2", 5);
    }

    boolean isGroup(String s) {
        return priorityMap.keySet().contains(s);
    }

    int getPriority(String group) {
        Integer p = priorityMap.get(group);
        if (p == null)
            return 0;
        else
            return p.intValue();
    }

    boolean shouldOverride(int priority, int c) {
        Integer p = charPriorityMap.get(c);
        if (p == null) return true;
        int pInt = p.intValue();
        return pInt > priority;
    }

    // return true if new entry is added
    // false otherwise
    boolean register(int priority, int unicode) {
        boolean alreadyExists = false;
        if (charPriorityMap.get(unicode) != null) {
            alreadyExists = true;
        }
        charPriorityMap.put(unicode, priority);
        return !alreadyExists;
    }

}
