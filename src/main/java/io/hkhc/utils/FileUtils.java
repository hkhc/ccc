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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtils {
	
	private static final String TAG = Consts.logTag("FU");

	private static final FileOptions defaultOptions = new FileOptions();

	public static String readFileToString(String path) throws IOException {
		return readFileToString(path, defaultOptions);
	}

	public static String readFileToString(String path, FileOptions options) throws IOException {
		return readStreamToString(new FileInputStream(path), options);
	}

	public static String readStreamToString(InputStream is) throws IOException {
		return readStreamToString(is, defaultOptions);
	}

	public static String readStreamToString(InputStream is, FileOptions options) throws IOException {
		
		Reader r = new InputStreamReader(is, options.getEncoding());
		
		StringBuilder builder = new StringBuilder();

		char[] buffer = new char[options.getBufferSize()];
		int len;
		int count=0;
		try {
			while ((len = r.read(buffer)) != -1) {
				builder.append(buffer, 0, len);
				count += len;
				options.getProgressCallback().progress(count);
			}
		}
		finally {
			r.close();
		}
		
		return builder.toString();
		
	}

	public static byte[] readFileToByteArray(String path) throws IOException {
		return readFileToByteArray(path, defaultOptions);
	}

	public static byte[] readFileToByteArray(String path, FileOptions options) throws IOException {
		return readStreamToByteArray(new FileInputStream(path), options);
	}

	public static byte[] readStreamToByteArray(InputStream is) throws IOException {
		return readStreamToByteArray(is, defaultOptions);
	}

	public static byte[] readStreamToByteArray(InputStream is, FileOptions options) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[options.getBufferSize()];
		int len;
		int count=0;
		try {
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
				count += len;
				options.getProgressCallback().progress(count);
			}
		}
		finally {
			is.close();
		}

		return baos.toByteArray();

	}

	public static int readStreamToByteArray(InputStream is, byte[] output, FileOptions options) throws IOException {

		int offset = 0;
		int len;
		while (offset<output.length
				&& (len=is.read(output, offset, output.length-offset))!=-1) {
			offset+=len;
			options.getProgressCallback().progress(offset);
		}

		return offset;

	}

	public static void writeStringToFile(String path, String value) throws IOException {
		writeStringToFile(path, value, defaultOptions);
	}

	public static void writeStringToFile(String path, String value, FileOptions options) throws IOException {
		writeStringToStream(new FileOutputStream(path), value, options);
	}


	public static void writeStreamToStream(OutputStream os, InputStream is) throws IOException {
		writeStreamToStream(os, is, defaultOptions);
	}

	public static void writeStreamToStream(OutputStream os, InputStream is,
										   FileOptions options) throws IOException {
		
		byte[] buffer = new byte[options.getBufferSize()];
		int len;
		int count = 0;
		try {
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, len);
				count += len;
				options.getProgressCallback().progress(count);
			}
		}
		finally {

			if (options.isCloseInputStream())
				is.close();
			if (options.isCloseOutputStream())
				os.close();
		}

	}

	public static void writeStringToStream(OutputStream os, String value) throws IOException {
		writeStringToStream(os, value, defaultOptions);
	}

	public static void writeStringToStream(OutputStream os, String value, FileOptions options) throws IOException {
		
		Writer w = new OutputStreamWriter(os, options.getEncoding());
		try {
			w.write(value);
		}
		finally {
			w.close();
		}

	}

	public static void writeBytesToFile(String path, byte[] data) throws IOException {
		writeBytesToFile(path, data, defaultOptions);
	}

	public static void writeBytesToFile(String path, byte[] data, FileOptions options) throws IOException {
		
		FileOutputStream os = new FileOutputStream(path);

		try {
			os.write(data);
		}
		finally {
			os.close();
		}
		
	}
	
	public static String getDir(String path) {
		int index = path.lastIndexOf(File.separator);
		if (index==-1) return "";
		else
			return path.substring(0,index);
	}
	
	public static boolean ensureDirectory(String path) {
		
		File f = new File(path);
        return ensureDirectory(f);

	}

    public static boolean  ensureDirectory(File path) {
        if (path.exists())
            return false;
        else
            return path.mkdirs();
    }

	private static boolean deleteFolder(File folder, int level, boolean deleteSelf) {

        boolean deleted = false;

	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                if (deleteFolder(f, level+1, true))
                        deleted = true;
	            } else {
	                if (f.delete()) deleted = true;
	            }
	        }
	    }
	    if (deleteSelf) {
            if (folder.delete()) deleted = true;
        }

        return deleted;

	}
	
	public static boolean deleteFolder(File folder) {
		return deleteFolder(folder, 0, true);
	}	
	
	public static boolean  deleteFolder(File folder, boolean deleteSelf) {
		return deleteFolder(folder, 0, deleteSelf);
	}


}
