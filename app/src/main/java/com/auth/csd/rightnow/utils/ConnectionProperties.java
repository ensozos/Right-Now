package com.auth.csd.rightnow.utils;

/**
 * Created by ilias on 1/7/2017.
 */

public class ConnectionProperties {

    private static final String url = "http://207.154.237.186:4444";

    private static final String login = "/user/login";
    private static final String register = "/user/register";

    public static String getLoginUrl() {
        return url + login;
    }

    public static String getRegisterUrl() {
        return url + register;
    }

}
