package com.zhisland.lib.load;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import okio.BufferedSink;

/**
 * 上传文件的RequestBody
 */
public class FileBody extends RequestBody {

    private final File file;
    private final long start;
    private final int count;

    public FileBody(final File file, long start,
                    int count) {
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
        this.start = start;
        this.count = count;
    }


    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        if (bufferedSink == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        RandomAccessFile raFile = new RandomAccessFile(this.file, "r");
        try {
            raFile.seek(start);
            byte[] tmp = new byte[count];
            int readCount = raFile.read(tmp, 0, count);
            bufferedSink.write(tmp, 0, readCount);
            bufferedSink.flush();
        } finally {
            raFile.close();
        }
    }
}
