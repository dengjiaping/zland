package com.zhisland.lib.async.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.FileBody;

public class RangeFileBody extends FileBody {

	private final File file;
	private final long start;
	private final int count;

	public RangeFileBody(final File file, final String mimeType, long start,
			int count) {
		super(file);
		if (file == null) {
			throw new IllegalArgumentException("File may not be null");
		}
		this.file = file;
		this.start = start;
		this.count = count;
	}

	public RangeFileBody(final File file, long start, int count) {
		this(file, "application/octet-stream", start, count);
	}

	/**
	 * @deprecated use {@link #writeTo(OutputStream)}
	 */
	@Deprecated
	public void writeTo(final OutputStream out, int mode) throws IOException {
		writeTo(out);
	}

	@Override
	public void writeTo(final OutputStream out) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		RandomAccessFile raFile = new RandomAccessFile(this.file, "r");
		try {
			raFile.seek(start);
			byte[] tmp = new byte[count];
			int readCount = raFile.read(tmp, 0, count);
			out.write(tmp, 0, readCount);
			out.flush();
		} finally {
			raFile.close();
		}
	}

	public String getTransferEncoding() {
		return MIME.ENC_BINARY;
	}

	public String getCharset() {
		return null;
	}

	public long getContentLength() {
		return count;
	}

	public String getFilename() {
		return this.file.getName();
	}

	public File getFile() {
		return this.file;
	}

}
