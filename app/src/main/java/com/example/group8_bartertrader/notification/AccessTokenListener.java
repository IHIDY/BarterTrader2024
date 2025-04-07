package com.example.group8_bartertrader.notification;

public interface AccessTokenListener {
    /**
     * receives access token
     * @param token
     */
    void onAccessTokenReceived(String token);

    /**
     * access token errer exception
     * @param exception
     */
    void onAccessTokenError(Exception exception);
}
