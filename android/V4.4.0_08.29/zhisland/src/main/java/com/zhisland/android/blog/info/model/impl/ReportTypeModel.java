package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.SectionInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 举报分类类型model
 * Created by Mr.Tui on 2016/7/6.
 */
public class ReportTypeModel implements IMvpModel {

    public static final String KEY_CACHE_REPORT_TYPE = "key_cache_report_type";

    private NewsApi api;

    public ReportTypeModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    public Observable<ArrayList<ReportType>> getReportType() {
        return Observable.create(new AppCall<ArrayList<ReportType>>() {
            @Override
            protected Response<ArrayList<ReportType>> doRemoteCall() throws Exception {
                Call<ArrayList<ReportType>> call = api.getReportType();
                return call.execute();
            }
        });
    }

    public ArrayList<ReportType> getReportTypeLocal() {
        Object obj = DBMgr.getMgr().getCacheDao().get(KEY_CACHE_REPORT_TYPE);
        if (obj != null) {
            return (ArrayList<ReportType>) obj;
        } else {
            ArrayList<ReportType> result = new ArrayList<ReportType>();
            ReportType reportType = new ReportType("news_report_reason_1", "欺诈");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_2", "色情");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_3", "政治谣言");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_4", "常识性谣言");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_5", "恶意营销");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_6", "其它侵权（冒名、诽谤、抄袭）");
            result.add(reportType);
            reportType = new ReportType("news_report_reason_7", "违规声明原创");
            result.add(reportType);
            return result;
        }
    }

    public void cacheReportType(ArrayList<ReportType> data) {
        if (data != null && data.size() > 0) {
            DBMgr.getMgr().getCacheDao().set(KEY_CACHE_REPORT_TYPE, data);
        }
    }

}