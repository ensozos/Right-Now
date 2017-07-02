package com.auth.csd.rightnow.utils;

/**
 * Created by ilias on 1/7/2017.
 */

public class ConnectionProperties {
    public static final String LOGIN_USERNAME_FIELD = "username";
    public static final String LOGIN_PASSWORD_FIELD = "pass";
    public static final String LOGIN_PLATFORM_FIELD = "platform";

    private static final String url = "http://207.154.237.186:4444";

    private static final String login = "/user/login";
    private static final String register = "/user/register";
    private static final String questList = "/user/quest/taken";

    public static String getLoginUrl() {
        return url + login;
    }

    public static String getRegisterUrl() {
        return url + register;
    }

    public static String getUserQuestTakenUrl() {
        return url + questList;
    }
}
