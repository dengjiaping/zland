package com.zhisland.lib.load;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.zhisland.lib.retrofit.ApiError;
import com.zhisland.lib.retrofit.AppCallBase;
import com.zhisland.lib.retrofit.RetrofitFactoryBase;
import com.zhisland.lib.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class LoadModel {

    private LoadTaskApi api;

    public LoadModel() {
        api = RetrofitFactoryBase.getInstance().getApi(com.zhisland.lib.component.application.ZHApplication.APP_CONFIG
                .getUploadBaseUrl(), LoadTaskApi.class);
    }

    public Observable<UpLoadResult> upload(final HttpUploadInfo upInfo) {
        return Observable.create(new AppCallBase<UpLoadResult>() {
            @Override
            protected Response<UpLoadResult> doRemoteCall() throws Exception {
                long start;
                int count;
                if (upInfo.totalBlocks == 1) {
                    start = 0;
                    count = (int) upInfo.size;
                } else {
                    if (upInfo.curBlock >= 0) {
                        start = upInfo.curBlock * upInfo.blockSize;
                        if (upInfo.size > (start + upInfo.blockSize)) {
                            count = upInfo.blockSize;
                        } else {
                            count = (int) (upInfo.size - start);
                        }
                    } else {
                        start = 0;
                        count = 0;
                    }
                }
                FileBody fileBody = new FileBody(new File(upInfo.filePath), start, count);
                Call<UpLoadResult> call = api.upload(upInfo.hashcode, upInfo.type, upInfo.ext, upInfo.time, upInfo.curBlock, upInfo.totalBlocks, upInfo.blockSize, fileBody);
                return call.execute();
            }
        });
    }

    public Observable<DownloadFileRes> download(final HttpDownloadInfo downInfo) {

        return Observable.create(new Observable.OnSubscribe<DownloadFileRes>() {
            @Override
            public void call(Subscriber<? super DownloadFileRes> subscriber) {
                String range = String.format(Locale.getDefault(), "bytes=%d-%d",
                        downInfo.endIndex, downInfo.endIndex
                                + LoadConstants.BIG_FILE_BLOCK_SIZE - 1);

                OkHttpClient mOkHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .addHeader(LoadConstants.RANGE_REQ, range)
                        .url(downInfo.downUrl)
                        .build();
                com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
                try {
                    com.squareup.okhttp.Response response = call.execute();
                    if (response.code() < 300) {
                        DownloadFileRes dfr = getRes(response);
                        if (dfr != null) {
                            File file = new File(downInfo.filePath);
                            RandomAccessFile raf = null;
                            ByteArrayOutputStream bos = null;
                            try {
                                raf = new RandomAccessFile(file, "rw");
                                if (raf.length() != dfr.totalSize) {
                                    raf.setLength(dfr.totalSize);
                                }
                                raf.seek(dfr.startIndex);
                                raf.write(response.body().bytes());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bos != null) {
                                    try {
                                        bos.close();
                                    } catch (Exception ex) {

                                    }
                                }
                                if (raf != null) {
                                    try {
                                        raf.close();
                                    } catch (Exception ex) {

                                    }
                                }
                            }
                            subscriber.onNext(dfr);
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onError(new ApiError());
            }
        });

    }

    private DownloadFileRes getRes(com.squareup.okhttp.Response response) throws IOException, NullPointerException {

        DownloadFileRes fileRes = null;
        String res = response.headers().get(LoadConstants.RANGE_RES);
        if (!StringUtil.isNullOrEmpty(res)) {
            String[] ss = res.split(" ");
            if (ss.length > 1) {
                String range = ss[1];
                String[] ranges = range.split("/");
                if (ranges.length > 1) {
                    fileRes = new DownloadFileRes();
                    String[] indexes = ranges[0].split("-");
                    fileRes.startIndex = Integer.parseInt(indexes[0]);
                    fileRes.endIndex = Integer.parseInt(indexes[1]);
                    fileRes.totalSize = Integer.parseInt(ranges[1]);
                    fileRes.contentLength = response.body().contentLength();
                    if (fileRes.endIndex >= (fileRes.totalSize - 1)) {
                        fileRes.isfinished = 1;
                    } else {
                        fileRes.isfinished = 0;
                    }
                }
            }
        } else {
            fileRes = new DownloadFileRes();
            fileRes.contentLength = response.body().contentLength();
            fileRes.isfinished = 1;
        }
        return fileRes;
    }

}
