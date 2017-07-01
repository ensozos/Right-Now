package com.auth.csd.rightnow.utils;

/**
 * Created by ilias on 1/7/2017.
 */

public class ConnectionProperties {

    private static final String url = "..";//TODO change names

    private static final String login = "/login";

    public static String getLoginUrl() {
        return url + login;
    }

}
