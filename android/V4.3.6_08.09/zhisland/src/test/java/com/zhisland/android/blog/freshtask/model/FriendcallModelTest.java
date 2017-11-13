package com.zhisland.android.blog.freshtask.model;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.retrofit.HeaderInterceptor;

import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by arthur on 2016/6/21.
 */
public class FriendcallModelTest {

    @Test
    public void testCallFriend() throws IOException {
        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new HeaderInterceptor());

        RequestBody formBody = new FormEncodingBuilder()
                .add("data", "[{\"userName\":\"闫向飞\"，\"userId\":6097982234285834243}]")
//                .add("data", "闫向飞")
                .add("taskName","TaskCallFriend")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.7.191:8090/bms-api-app/relation/task/summon/friend/apply")
                .addHeader("device_id", "015096114100252099024169043158019198093183205060")
                .addHeader("version", "4.3.2")
                .addHeader("os", "android")
                .addHeader("apiVersion", "1.0")
                .addHeader("uid", "6097982234285834243")
                .addHeader("atk", "12d70992d2494a56ad5d6ec15b07f3c5")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());

    }
}
