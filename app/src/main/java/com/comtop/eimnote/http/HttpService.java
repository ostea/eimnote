package com.comtop.eimnote.http;

import com.comtop.eimnote.http.parser.GetNoteResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryName;

/**
 * author chaos
 * Description:
 * DATE: 2018/5/4
 * Email: oscc92@gmail.com
 */
public interface HttpService {

    @GET("tag/get")
    Observable<GetNoteResponse> loadLabelList(@QueryName String name);

}
