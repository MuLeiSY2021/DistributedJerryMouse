package pers.jerrymouse.servlet.session;

public interface Session {
    String getId();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    long getCreationTime();

    long getLastAccessedTime();

    void setLastAccessedTime(long lastAccessedTime);

    void invalidate();
}
