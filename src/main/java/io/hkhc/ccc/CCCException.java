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

/**
 * Created by herman on 3/12/2017.
 */

public class CCCException extends Exception {

    public CCCException() {
        super();
    }

    public CCCException(String detailMessage) {
        super(detailMessage);
    }

    public CCCException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CCCException(Throwable throwable) {
        super(throwable);
    }
}
