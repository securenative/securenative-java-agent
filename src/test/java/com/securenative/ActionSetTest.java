package com.securenative;

import com.securenative.actions.ActionSet;
import com.securenative.models.SetType;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ActionSetTest {
    @Test
    public void blockUserIpCountryForever() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ip, null);
        actionSet.add(SetType.USER.name(), user, null);
        actionSet.add(SetType.COUNTRY.name(), country, null);

        Assert.assertTrue(actionSet.has(SetType.IP.name(), ip, null));
        Assert.assertTrue(actionSet.has(SetType.USER.name(), user, null));
        Assert.assertTrue(actionSet.has(SetType.COUNTRY.name(), country, null));
    }

    @Test
    public void blockUserIpCountryTemp() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";
        Long timeout = 2L;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ip, timeout);
        actionSet.add(SetType.USER.name(), user, timeout);
        actionSet.add(SetType.COUNTRY.name(), country, timeout);

        try {
            TimeUnit.SECONDS.sleep(timeout+1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip, timeout));
        Assert.assertFalse(actionSet.has(SetType.USER.name(), user, timeout));
        Assert.assertFalse(actionSet.has(SetType.COUNTRY.name(), country, timeout));

    }

    @Test
    public void expireFirstIp() {
        String ip = "10.0.0.1";
        String ip2 = "10.0.0.2";
        long timeout = 2L;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ip, 1L);
        actionSet.add(SetType.IP.name(), ip2, 10L);

        try {
            TimeUnit.SECONDS.sleep(timeout+1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip, 1L));
        Assert.assertTrue(actionSet.has(SetType.IP.name(), ip2, 10L));
    }

    @Test
    public void expireFirstUser() {
        String user = "12345";
        String user2 = "333333";
        long timeout = 2L;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.USER.name(), user, 1L);
        actionSet.add(SetType.USER.name(), user2, 10L);

        try {
            TimeUnit.SECONDS.sleep(timeout+1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(actionSet.has(SetType.USER.name(), user, 1L));
        Assert.assertTrue(actionSet.has(SetType.USER.name(), user2, 10L));
    }

    @Test
    public void blockIpRangeForever() {
        String ipRange = "2.3.4.5/32";
        String validIP = "2.3.4.5";
        String invalidIP = "1.2.3.4";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ipRange, null);

        Assert.assertTrue(actionSet.has(SetType.IP.name(), validIP, null));
        Assert.assertFalse(actionSet.has(SetType.IP.name(), invalidIP, null));
    }

    @Test
    public void ignoreInvalidIp() {
        String badIP = "2.3.45";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), badIP, null);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), badIP, null));
    }

    @Test
    public void deleteNotExisted() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.delete(SetType.IP.name(), ip, null);
        actionSet.delete(SetType.USER.name(), user, null);
        actionSet.delete(SetType.COUNTRY.name(), country, null);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip, null));
        Assert.assertFalse(actionSet.has(SetType.USER.name(), user, null));
        Assert.assertFalse(actionSet.has(SetType.COUNTRY.name(), country, null));
    }
}