package com.securenative.agent.actions;

import com.google.common.net.InetAddresses;
import com.securenative.agent.utils.SchedulerUtils;
import com.securenative.agent.models.ActionItem;
import com.securenative.agent.enums.SetType;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ActionSet {
    private String name;
    private Set<ActionItem> ip;
    private Set<ActionItem> user;
    private Set<ActionItem> country;
    private static final Logger logger = Logger.getLogger(ActionSet.class.getName());

    public ActionSet(String name) {
        this.name = name;
        this.ip = new HashSet<>();
        this.user = new HashSet<>();
        this.country = new HashSet<>();
    }

    public void add(String type, String item, @Nullable Long timeout) {
        if (timeout != null) {
            SchedulerUtils.delayedExecutor(timeout, TimeUnit.SECONDS, () -> this.delete(type, item, timeout));
        }

        if (type.equals(SetType.IP.name())) {
            if (this.isValidIP(item)) {
                this.ip.add(new ActionItem(item, timeout));
            } else {
                logger.fine(String.format("Not a valid ip range! %s", item));
            }
        } else if (type.equals(SetType.USER.name())) {
            this.user.add(new ActionItem(item, timeout));
        } else if (type.equals(SetType.COUNTRY.name())) {
            this.country.add(new ActionItem(item, timeout));
        }
    }

    public boolean has(String type, String item, @Nullable Long timeout) {
        return this.contains(type, item, timeout);
    }

    public void delete(String type, String item, @Nullable Long timeout) {
        if (type.equals(SetType.IP.name())) {
            this.ip.remove(new ActionItem(item, timeout));
        } else if (type.equals(SetType.USER.name())) {
            this.user.remove(new ActionItem(item, timeout));
        } else if (type.equals(SetType.COUNTRY.name())) {
            this.country.remove(new ActionItem(item, timeout));
        }
    }

    private boolean isValidIP(String ip) {
        if (ip.contains("/")) {
            // subnet
            String subnet[] = ip.split("/");
            return InetAddresses.isInetAddress(subnet[0]);
        }
        return InetAddresses.isInetAddress(ip);
    }

    public String getName() {
        return name;
    }

    public Set<ActionItem> getIp() {
        return ip;
    }

    public Set<ActionItem> getUser() {
        return user;
    }

    public Set<ActionItem> getCountry() {
        return country;
    }

    public Boolean contains(String type, String item, Long timeout) {
        ActionItem ai = new ActionItem(item, timeout);
        if (type.equals(SetType.IP.name())) {
            for (ActionItem actionItem : this.ip) {
                if (actionItem.getItem().contains("/")) {
                    boolean sameIp = subnetContains(ai.getItem(), actionItem.getItem());
                    boolean sameTimeout;
                    if (timeout != null) {
                        sameTimeout = actionItem.getTimeout().equals(timeout);
                    } else {
                        sameTimeout = true;
                    }
                    return sameIp && sameTimeout;
                } else {
                    if (actionItem.equals(ai)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (type.equals(SetType.COUNTRY.name())) {
            for (ActionItem actionItem : this.country) {
                if (actionItem.equals(ai)) {
                    return true;
                }
            }
            return false;
        }
        if (type.equals(SetType.USER.name())) {
            for (ActionItem actionItem : this.user) {
                if (actionItem.equals(ai)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean subnetContains(String ip, String subnet) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(subnet);
        return ipAddressMatcher.matches(ip);
    }
}
