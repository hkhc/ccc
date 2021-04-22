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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by herman on 16/12/2017.
 */
public class SourceParser {

    private BufferedReader reader;
    private List<String> currLineParts = Collections.EMPTY_LIST;
    private int currCode = 0;
    private CharEntry currEntry = null;
    private Prioritizer prioritizer = null;
    private Pattern tokenSplittingPattern;

    enum TokenType {
        ccc, unicode, group, unknown
    }

    static class Token {
        public TokenType type;
        public String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object o) {

            if (!(o instanceof Token)) return false;

            Token other = (Token)o;
            if (this.type!=other.type) return false;
            if ((this.value==null) ^ (other.value==null)) return false;
            if (this.value!=null && !this.value.equals(other.value)) return false;
            return true;
        }

        public String toString() {
            return "type="+type + " value="+value;
        }

    }

    public SourceParser(Prioritizer prioritizer, InputStream is) throws IOException {

        if (prioritizer==null) throw new NullPointerException("null prioritizer");
        if (is==null) throw new NullPointerException("null input stream");

        this.prioritizer = prioritizer;
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        reader = new BufferedReader(isr);

        tokenSplittingPattern = Pattern.compile("[ \t]");

    }

    List<String> getCurrLineParts() {
        return currLineParts;
    }

    // return true for a new list of parts
    // false if non-new list of parts
    // null if EOF
    // It ensure the currLineParts filled with current line part
    // If the parts list is empty, read another line and parse it
    // It the parts list is not empty, just return the parts list
    Boolean fillLineParts() throws IOException {
        boolean newLine = false;

        while (currLineParts.isEmpty()) {
            String line = reader.readLine();
            newLine = true;
            if (line == null) {
                currLineParts = null;
                return null;
            } else {
                currLineParts = new ArrayList<>();
                String[] parts = tokenSplittingPattern.split(line);
                for (String s : parts) {
                    if (!s.trim().isEmpty()) {
                        currLineParts.add(s);
                    }
                }
            }
        }
        return newLine;
    }

    Token getNextToken() throws IOException {

        Boolean newParts = fillLineParts();
        if (newParts == null) {
            return null;
        }
        // at this point, the currLineParts is not empty
        String val = currLineParts.remove(0);
        if (newParts) {
            return new Token(TokenType.ccc, val);
        } else if (prioritizer.isGroup(val)) {
            return new Token(TokenType.group, val);
        } else if (val.startsWith("U+")) {
            return new Token(TokenType.unicode, val);
        } else {
            return new Token(TokenType.unknown, val);
        }

    }

    boolean handleNextToken(Token token) throws IOException {

        boolean finished = false;
        switch (token.type) {
            case ccc:
                currCode = Integer.parseInt(token.value);
                break;
            case group:
                currEntry = new CharEntry();
                currEntry.setCcc(currCode);
                currEntry.setGroup(token.value);
                break;
            case unicode:
                if (currEntry == null) {
                    currEntry = new CharEntry();
                    currEntry.setCcc(currCode);
                }
                int unicode = Integer.parseInt(token.value.substring(2), 16);
                currEntry.setUnicode(unicode);
                finished = true;
                break;
            case unknown:
                // ignore
        }

        return finished;

    }

    public CharEntry getNextEntry() throws IOException {

        boolean finished = false;
        while (!finished) {
            Token nextToken = getNextToken();
            if (nextToken == null) {
                return null;
            }
            finished = handleNextToken(nextToken);
        }
        CharEntry entry = currEntry;
        currEntry = null;
        return entry;

    }

    int getCurrCode() {
        return currCode;
    }

    CharEntry getCurrEntry() {
        return currEntry;
    }
}
