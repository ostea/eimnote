package com.comtop.eimnote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.comtop.eimnote.NoteApp;
import com.comtop.eimnote.R;
import com.comtop.eimnote.base.BaseActivity;
import com.comtop.eimnote.http.HttpService;
import com.comtop.eimnote.http.RetrofitManager;
import com.comtop.eimnote.http.parser.GetNoteResponse;
import com.comtop.eimnote.util.AbstractObserver;
import com.comtop.eimnote.util.CLog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class APITestActivity extends BaseActivity {
    private static final String TAG = APITestActivity.class.getSimpleName();

    private HttpService httpService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_api_test);

        RetrofitManager manager = new RetrofitManager(this);
        httpService = manager.createService(NoteApp.getBaseUrl(), HttpService.class);
    }

    public void onRequestApi(View view) {
        httpService.loadLabelList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbstractObserver<GetNoteResponse>() {
                    @Override
                    public void onNext(GetNoteResponse getNoteResponse) {
                        super.onNext(getNoteResponse);
                        CLog.i(TAG, getNoteResponse.getMessage());
                    }
                });
    }

}
