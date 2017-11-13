package com.zhisland.lib.load;

import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

public interface LoadTaskApi {

    @Multipart
    @POST("upload/uploadFile")
    Call<UpLoadResult> upload(@Part("fhashcode") String fhashcode, @Part("type") int type, @Part("ext") String ext,
                              @Part("time") long time, @Part("cblock") long cblock, @Part("tblocks") long tblocks, @Part("block_size") int block_size,
                              @Part(("file\";filename=bbb.aaa\"")) FileBody file);

}
