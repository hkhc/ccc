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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper methods for Strings
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class StringUtils {

    private static final String TAG = Consts.logTag("SU");

    /**
     * Check if the CharSequence object is null or empty.
     * @param cs CharSequence to be checked
     * @return true if the parameter is null or has length 0.
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs==null || cs.length()==0;
    }

    /**
     * Avoiding null CharSequence.
     * @param cs the CharSequence to be checked
     * @return return empty String ("") if the parameter is null. return the original reference otherwise.
     */
    public static CharSequence optional(CharSequence cs) {
        if (cs==null) return "";
        return cs;
    }

    /**
     * Check if the length of String is within the range (inclusive) specified.
     * For example,
     * StringUtils.lengthInRange("Apple", 3,10) == true
     * StringUtils.lengthInRange("Apple", 6,10) == false
     * @param cs The String to be checked
     * @param min The lower bound of range (inclusive)
     * @param max The upper bound of range (inclusive)
     * @return true if the length of the CharSequence is in range, False otherwise.
     */
    public static boolean lengthInRange(CharSequence cs, @SuppressWarnings("SameParameterValue") int min, int max) {
        return cs.length()>=min && cs.length()<=max;
    }

    public static boolean lengthWithRange2(CharSequence cs, int len1, int len2) {
        return cs.length()==len1 || cs.length()==len2;
    }

    /**
     * Check if two CharSequences are equal, in sense of standard .equals() contract, and additionally
     * it allow null reference.
     * @param a CharSequence to be compared
     * @param b CharSequence to be compared
     * @return true if the CharSequence are equals, or two CharSequences are null. False otherwise
     */
    public static boolean equalCharSequence(CharSequence a, CharSequence b) {

        if ((a==null)&&(b==null)) return true;
        return (a == null) == (b == null) && a.toString().equals(b.toString());

    }

    public interface IdeographicChecker {
        boolean isIdeographic(int c);
    }

    public static class Jdk7IdeographicChecker implements IdeographicChecker {

        private static Method checkerMethod;

        static {
            try {
                System.out.println("Init Jdk7IdeographicChecker");
                checkerMethod = Character.class.getMethod("isIdeographic", int.class);
            } catch (NoSuchMethodException e) {
                checkerMethod = null;
            }
        }

        @Override
        public boolean isIdeographic(int c) {
            if (checkerMethod==null) {
                return new OldIdeographicChecker().isIdeographic(c);
            }
            else {
                try {
                    return (boolean)checkerMethod.invoke(null, (Integer)c);
                } catch (IllegalAccessException e) {
                    return false;
                } catch (InvocationTargetException e) {
                    return false;
                }
            }
        }
    }

    public static class OldIdeographicChecker implements IdeographicChecker {
        private static int[] hansRange = {
                0x4e00, 0x9fff,
                0x2e80, 0x2fff,
                0xf900, 0xfaff
        };
        @Override
        public boolean isIdeographic(int c) {
            for(int i=0;i<hansRange.length;i+=2) {
                if (c>=hansRange[i] && c<=hansRange[i+1]) return true;
            }
            return false;
        }
    }

    /**
     * Get an object to Check if a character is ideographic characters like Chinese, Korean, Hiragana, Katakana, etc.
     * For API level 19 or Jdk 7 or above, we use the standard API for the check. we made our own check for level&lt;19.
     * @return the implementation of ideographic checker.
     */
    public static IdeographicChecker getIdeographicChecker() {

        Class<?> clazz = Character.class;
        try {
            Method method = clazz.getMethod("isIdeographic", int.class);
            return new Jdk7IdeographicChecker();
        }
        catch (NoSuchMethodException e) {
            return new OldIdeographicChecker();
        }

    }

    /**
     * Check if a string is parsable to number, without throwing exception
     * @param s the String to be check
     * @return true if the String can be parsed as integer. Falase otherwise
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if all strings in the paramater lists are empty
     * @param strs is the list of strings
     * @return true if all the strings are empty. False otherwise
     */
    public static boolean allEmpty(CharSequence... strs) {
        for(CharSequence s : strs) {
            if (isEmpty(s)) return false;
        }
        return true;
    }

    /**
     * Check if all strings in the paramater lists are not empty
     * @param strs is the list of strings
     * @return true if all the strings are not empty. False otherwise
     */
    public static boolean allNotEmpty(CharSequence... strs) {
        for(CharSequence s : strs) {
            if (isEmpty(s)) return false;
        }
        return true;
    }

    /**
     * Check if *any* strings in the paramater lists are empty
     * @param strs is the list of strings
     * @return true if any of the strings is empty. False otherwise
     */
    public static boolean anyEmpty(CharSequence... strs) {
        for(CharSequence s : strs) {
            if (isEmpty(s)) return true;
        }
        return false;
    }

    /**
     * Check if *any* strings in the paramater lists are not empty
     * @param strs is the list of strings
     * @return true if any of the strings is not empty. False otherwise
     */
    public static boolean anyNotEmpty(CharSequence... strs) {
        for(CharSequence s : strs) {
            if (!isEmpty(s)) return true;
        }
        return false;
    }

}
