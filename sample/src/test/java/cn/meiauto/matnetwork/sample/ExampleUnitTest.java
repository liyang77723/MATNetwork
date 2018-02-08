package cn.meiauto.matnetwork.sample;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will enqueue on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        File dir = new File("c:/ss/sss/");
        System.out.print(dir.mkdirs());
    }

    public static void main(String[] strings) {
        File dir = new File("c:/ss/");
        System.out.print(dir.mkdirs());
    }
}