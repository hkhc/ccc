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

package io.hkhc.utils;

/**
 * Created by herman on 5/1/2018.
 */

public class FileOptions {

    public static final String DEFAULT_ENCODING="utf-8";
    public static final int DEFAULT_BUFFER_SIZE = 16384;

    // --------------

    private int bufferSize = DEFAULT_BUFFER_SIZE;

    public FileOptions bufferSize(int size) {
        bufferSize = size;
        return this;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    // --------------

    public interface Progress {
        void progress(int count);
    }

    private Progress progressCallback = new Progress() {
        @Override
        public void progress(int count) {
            // do nothing intentionally
        }
    };

    public FileOptions progressCallback(Progress callback) {
        this.progressCallback = callback;
        return this;
    }

    public Progress getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(Progress progressCallback) {
        this.progressCallback = progressCallback;
    }

    // --------------

    private String encoding = DEFAULT_ENCODING;

    public FileOptions encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    // --------------

    private boolean closeInputStream = true;

    public FileOptions closeInputStream(boolean close) {
        this.closeInputStream = close;
        return this;
    }

    public boolean isCloseInputStream() {
        return closeInputStream;
    }

    public void setCloseInputStream(boolean closeInputStream) {
        this.closeInputStream = closeInputStream;
    }

    // --------------

    private boolean closeOutputStream = true;

    public FileOptions closeOutputStream(boolean close) {
        this.closeOutputStream = close;
        return this;
    }

    public boolean isCloseOutputStream() {
        return closeOutputStream;
    }

    public void setCloseOutputStream(boolean closeOutputStream) {
        this.closeOutputStream = closeOutputStream;
    }

}
