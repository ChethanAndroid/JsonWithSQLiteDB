package com.example.chethan.jsonwithsqlitedb;

public class ServerLinkPojo {

    private static String ApiLink = "https://reqres.in/api/unknown/2";
    private static String SecLink = "https://reqres.in/api/unknown";


    public static String getSecLink() {
        return SecLink;
    }

    public static void setSecLink(String secLink) {
        SecLink = secLink;
    }

    public String getApiLink() {
        return ApiLink;
    }

    public void setApiLink(String apiLink) {
        ApiLink = apiLink;
    }


}
