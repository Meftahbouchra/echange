package com.bouchra.myapplicationechange.notification;

public class Token {
    /* an FCM (Firebase Cloud Messaging) token, or much commonly known as a registrationToken .An ID  issued by GCM
    connection servers to the client app that allows it  to receiver massages
     */
    String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
