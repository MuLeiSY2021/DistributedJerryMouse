package pers.jerrymouse.warpper.session;

import pers.jerrymouse.servlet.session.Session;

import java.util.HashMap;
import java.util.Map;

public class CustomSession implements Session {

    private final String id;
    private final Map<String, Object> attributes;
    private final long creationTime;
    private long lastAccessedTime;

    public CustomSession(String id) {
        this.id = id;
        this.attributes = new HashMap<>();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public void invalidate() {
        attributes.clear();
    }

}
