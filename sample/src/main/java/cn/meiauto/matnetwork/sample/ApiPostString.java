package cn.meiauto.matnetwork.sample;

import cn.meiauto.matrxretrofit.base.result.BaseResult;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/28
 */
public interface ApiPostString {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("location/fence/updateFence")
    Observable<BaseResult> updateFence(@Body RequestBody body);

    class UpdateFence {

        String id;
        String title;
        int radius;
        PointBean point;
        int status;
        int pullOutStatus;
        int pullInStatus;
        String center;
        String shapeType;
        String radiusUnits;

        public UpdateFence(String id, String title, int radius, double lat, double lng,
                           int status, int pullOutStatus, int pullInStatus,
                           String center, String shapeType, String radiusUnits) {
            this.id = id;
            this.title = title;
            this.radius = radius;
            this.point = new PointBean(lat, lng);
            this.status = status;
            this.pullOutStatus = pullOutStatus;
            this.pullInStatus = pullInStatus;
            this.center = center;
            this.shapeType = shapeType;
            this.radiusUnits = radiusUnits;
        }

        class PointBean {

            double lat;
            double lng;

            PointBean(double latitude, double longitude) {
                this.lat = latitude;
                this.lng = longitude;
            }
        }
    }
}
