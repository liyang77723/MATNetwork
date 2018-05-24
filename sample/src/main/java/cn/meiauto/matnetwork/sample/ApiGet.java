package cn.meiauto.matnetwork.sample;

import cn.meiauto.matrxretrofit.base.result.BaseDataResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/4
 */
public interface ApiGet {

    @GET("user/account/queryUser")
    Observable<BaseDataResult<QueryUserData>> queryUser();

    class QueryUserData {

        public int id;
        public String username;
        public String password;
        public String type;
        public int status;
        public long createdTime;
        public long updatedTime;

        @Override
        public String toString() {
            return "↓QueryUserData↓" +
                    "\n    id=" + id +
                    "\n    username=" + username +
                    "\n    password=" + password +
                    "\n    type=" + type +
                    "\n    status=" + status +
                    "\n    createdTime=" + createdTime +
                    "\n    updatedTime=" + updatedTime +
                    "\n↑QueryUserData↑";
        }
    }
}
