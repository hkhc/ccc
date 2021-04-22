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

import java.io.IOException;
import java.io.InputStream;

/**
 * This class wrap up another InputStream so that each read method invocation will return data not
 * more than specified "readSize" bytes. It is used to test against the class that read large
 * amount of data from IO (file/network), and make sure it can handle small trunk data from read
 * method.
 */

public class SizeThrottledInputStream extends InputStream {

    private InputStream delegate;
    private int readSize;

    public SizeThrottledInputStream(InputStream delegate, int size) {
        this.delegate = delegate;
        this.readSize = size;
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    @Override
    public int available() throws IOException {
        return delegate.available();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public void mark(int readlimit) {
        delegate.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        if (buffer.length>readSize) {
            return delegate.read(buffer, 0, readSize);
        }
        else {
            return delegate.read(buffer);
        }
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        System.out.println("original readsize " + byteCount + " changed to " + readSize);
        if (byteCount>readSize) {
            return delegate.read(buffer, byteOffset, readSize);
        }
        else {
            return delegate.read(buffer, byteOffset, byteCount);
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        delegate.reset();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        if (byteCount>readSize) {
            return delegate.skip(readSize);
        }
        else {
            return delegate.skip(byteCount);
        }
    }

}
