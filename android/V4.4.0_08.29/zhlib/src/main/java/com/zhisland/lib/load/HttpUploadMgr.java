package com.zhisland.lib.load;

import com.zhisland.lib.util.MLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpUploadMgr extends BaseLoadMgr<HttpUploadInfo> {

    private LoadDbHelper dbHelper;

    public HttpUploadMgr() {
        super();
        dbHelper = LoadDbHelper.getHelper();
    }

    @Override
    public List<Long> fetchLoad(int count, ArrayList<Long> curLoads) {
        return dbHelper.getHttpUpDao().fetchLoadInfo(count, curLoads);
    }

    @Override
    public void loadFile(final HttpUploadInfo upInfo) {

        LoadModel model = new LoadModel();
        model.upload(upInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UpLoadResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.e(TAG,"loadFile onError. \n" + e);
                        failLoad(upInfo.token, LoadStatus.ERROR);
                    }

                    @Override
                    public void onNext(UpLoadResult content) {

                        if (content != null && content.data != null) {
                            if (content.data.isfinished == 1) {
                                if (content.data.picItem != null) {
                                    upInfo.picId = content.data.picItem.id;
                                    upInfo.picUrl = content.data.picItem.url;
                                    try {
                                        dbHelper.getHttpUpDao().update(
                                                upInfo);
                                        MLog.e(TAG, upInfo.hashcode + "finish ");
                                        finishLoad(upInfo.token, null);
                                    } catch (SQLException e) {
                                        failLoad(upInfo.token, LoadStatus.ERROR);
                                    }
                                }

                            } else {
                                int lastBlock = content.data.cblock;
                                HttpUploadInfo info = upInfo;
                                info.curBlock = lastBlock + 1;
                                int i = dbHelper.getHttpUpDao().update(
                                        info.token, (int) info.curBlock);
                                postEvent(info.token);
                                if (i < 0) {
                                    failLoad(info.token, LoadStatus.ERROR);
                                } else {
                                    loadFile(info);
                                }
                                MLog.d(TAG, "upload " + info.curBlock + " of "
                                        + info.totalBlocks);
                            }
                        }
                    }
                });
    }

    @Override
    public HttpUploadInfo getLoadInfo(long token) {
        try {
            return dbHelper.getHttpUpDao().queryForId(token);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public int updateStatusByToken(long token, int status) {
        return dbHelper.getHttpUpDao().updateLoadStatus(token, status);
    }

    @Override
    public long insert(HttpUploadInfo info) {
        return dbHelper.getHttpUpDao().insertLoadInfo(info);
    }

    @Override
    public int stopByOwnerId(long ownerId) {
        return dbHelper.getHttpUpDao().stopLoad(ownerId);
    }

    @Override
    public int deleteByOwnerId(long ownerId) {
        return dbHelper.getHttpUpDao().deleteLoad(ownerId);
    }

    @Override
    public int updateStatusByOwnerId(long ownerId, int status) {
        return dbHelper.getHttpUpDao().updateLoadStatusByOwner(ownerId,
                status);
    }

    @Override
    public int deleteByToken(long token) {
        return dbHelper.getHttpUpDao().deleteByToken(token);
    }

}
