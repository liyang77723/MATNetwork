package cn.meiauto.matnetwork.sample;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will enqueue on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cn.meiauto.matnetwork", appContext.getPackageName());

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().build();
        Call call = okHttpClient.newCall(request);
        retrofit2.Call
    }
}
