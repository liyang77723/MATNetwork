package cn.meiauto.matnetwork.sample;

import cn.meiauto.matrxretrofit.base.result.BaseDataResult;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/4
 */
public interface ApiPost {
    @POST("user/account/login")
    Observable<BaseDataResult<LoginData>> login(
            @Query("username") String phone,
            @Query("password") String password,
            @Query("device.deviceId") String deviceId
    );

    class LoginData {

        public int accountId;
        public String token;

        @Override
        public String toString() {
            return "↓LoginData↓" +
                    "\naccountId=" + accountId +
                    "\ntoken=" + token +
                    "\n↑LoginData↑";
        }
    }
}
