package com.securenative;

import com.securenative.utils.Logger;
import com.securenative.utils.Utils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {
    @BeforeClass
    public static void setup() {
        Logger.initLogger();
        Logger.configureLogger();
    }

    @Test
    public void testDecryption() throws Exception {
        String cookie = "821cb59a6647f1edf597956243e564b00c120f8ac1674a153fbd707da0707fb236ea040d1665f3d294aa1943afbae1b26b2b795a127f883ec221c10c881a147bb8acb7e760cd6f04edc21c396ee1f6c9627d9bf1315c484a970ce8930c2ed1011af7e8569325c7edcdf70396f1abca8486eabec24567bf215d2e60382c40e5c42af075379dacdf959cb3fef74f9c9d15";
        String apiKey = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";

        String a = Utils.decrypt(cookie, apiKey);
        String e = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";

        Assert.assertEquals(e,a);
    }

    @Test
    public void testEncryptionDecryption() throws Exception {
        String apiKey = "6EA4915349C0AAC6F6572DA4F6B00C42DAD33E75";
        String e = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";

        String encrypted = Utils.encrypt(e, apiKey);
        String a = Utils.decrypt(encrypted, apiKey);

        Assert.assertNotNull(encrypted);
        Assert.assertEquals(e,a);
    }
}
