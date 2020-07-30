package com.securenative.agent.models;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ActionItem {
    private String item;
    private Long timeout;

    public ActionItem(String item, @Nullable Long timeout) {
        this.item = item;
        this.timeout = timeout;
    }

    public String getItem() {
        return item;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionItem)) return false;
        ActionItem that = (ActionItem) o;
        return Objects.equals(item, that.item) &&
                Objects.equals(timeout, that.timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, timeout);
    }
}
