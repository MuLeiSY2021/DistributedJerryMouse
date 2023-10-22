package pers.jerrymouse.warpper.session;

import pers.jerrymouse.servlet.session.Cookie;

public class CustomCookie implements Cookie {
    public static final String COOKIE_NAME = "JERRY_MOUSE";
    private final String name;
    private final String value;

    // 构造函数
    public CustomCookie(String value) {
        this.name = CustomCookie.COOKIE_NAME;
        this.value = value;
    }

    // 转化为字符串的形式
    @Override
    public String toString() {
        return name + "=" + value + "; ";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
