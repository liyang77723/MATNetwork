package cn.meiauto.matnetwork.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.meiauto.matnetwork.callback.StringCallback;
import cn.meiauto.matnetwork.sample.requests.BaiduRequest;
import cn.meiauto.matutils.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);
        }
    }

    public void requestBaidu(View view) {
        new BaiduRequest.Builder("http://www.li-yang.org").build().execute(new StringCallback(this) {
            @Override
            public void onResponse(int flag, String result) {
                LogUtil.debug("onResponse() called with: flag = [" + flag + "], result = [" + result.replaceAll("\\s", "") + "]");
            }
        });
    }
}