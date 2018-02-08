package cn.meiauto.matnetwork;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.meiauto.matnetwork.callback.StringCallback;
import cn.meiauto.matnetwork.request.PostRequest;

import static org.junit.Assert.assertEquals;

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
        assertEquals("cn.meiauto.matnetwork.test", appContext.getPackageName());
    }
}
