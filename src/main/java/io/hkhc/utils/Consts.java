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

package io.hkhc.utils;

/**
 * Created by pandac on 2/12/14.
 */
@SuppressWarnings("unused")
public class Consts {

    public static String META_TAG = "WU";

    public static void setMetaTag(String tag) {
        META_TAG = tag;
    }

    public static String logTag(String t) {
        return META_TAG+"/"+t;
    }

}
