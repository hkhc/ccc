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

package io.hkhc.ccc;

import java.text.DecimalFormat;

import io.hkhc.ccc.internal.Source;

// create CCC lookup table
public class CCCDB {

    private static final String TAG = Consts.logTag("CCCDB");

    private DecimalFormat df = new DecimalFormat("0000");
	private Source source = null;

	public CCCDB() {
		this(new ResourceSource());
	}

	public CCCDB(Source source) {
		this.source = source;
	}


	public int getCCC(char c) {

		return source.getCCC(c);

	}

	public void load() throws CCCException {
		source.load();
	}

	public void load(String destFilename) throws CCCException {
		source.load(destFilename);
	}

	public String getCCCs(CharSequence s) {

		StringBuilder b = new StringBuilder();
        boolean first = true;
		for(int i=0;i<s.length();i++) {
			int code = getCCC(s.charAt(i));
            if (!first) b.append(" ");
			if (code==0)
				b.append("0000");
			else
				b.append(df.format(code));
            first = false;
		}
		return b.toString();

	}

	public int getCodeCount() {
		return source.getCodeCount();
	}

}
