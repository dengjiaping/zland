package com.zhisland.lib.retrofit;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;
import com.zhisland.lib.util.MLog;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * Created by Mr.Tui on 2016/6/15.
 */
final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    ResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Reader reader = value.charStream();
        BufferedReader in = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        String responseBody = buffer.toString();
        MLog.e("zhapp", "responseBody ： " + responseBody);
        try {
            return gson.fromJson(responseBody, type);
        } finally {
            closeQuietly(reader);
        }
    }

    void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }
}
