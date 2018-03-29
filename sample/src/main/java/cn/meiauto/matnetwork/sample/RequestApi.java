package cn.meiauto.matnetwork.sample;

import java.util.Map;

import cn.meiauto.matnetwork.Result;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestApi {


//            Call<Result> checkCall = checks.checkPin("18935028851", pin);
//            checkCall.enqueue(new Callback<Result>() {
//                @Override
//                public void onResponse(Call<Result> call, Response<Result> response) {
//                    Result result = response.body();
//                    LogUtil.debug("onResponse() called with: call = [" + call + "], result = [" + result + "]");
//                }
//
//                @Override
//                public void onFailure(Call<Result> call, Throwable t) {
//                    LogUtil.debug("onFailure() called with: call = [" + call + "], t = [" + t + "]");
//                }
//            });

//    @GET("vehicle/tservice/checkPinOfDefault")
//    Call<String> checkPin(@Query("userName") String phone, @Query("pin") String pin);

    @GET("vehicle/tservice/checkPinOfDefault")
    Observable<Result> checkPin(@Query("userName") String phone, @Query("pin") String pin);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user-remote/sendCommand")
    Observable<Result<SendCommandData>> sendCommand(@Header("appId") String appId, @Body RequestBody body);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("user-remote/sendCommand")
    Observable<Result> sendCommand(@Header("appId") String appId, @HeaderMap Map map);

    @GET("user-remote/findSendCommandStatus")
    Observable<Result<RCQueryBean>> queryCommand(@Query("sendId") String sendId);

    @GET("vehicle/tservice/getInfoByVin?vin=1")
    Observable<Result<RCQueryBean>> get();

    @POST("vehicle/tservice/openTService")
    Observable<Result<RCQueryBean>> post();
}