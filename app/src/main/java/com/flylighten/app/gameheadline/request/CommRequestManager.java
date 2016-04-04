package com.flylighten.app.gameheadline.request;

public class CommRequestManager {

    private static CommRequestManager sInstance;

    public static CommRequestManager getInstance() {
        if (sInstance == null) {
            sInstance = new CommRequestManager();
        }
        return sInstance;
    }

    public void prepareRequest(in.srain.cube.request.RequestBase request) {

        // you can add some basic data to here
        String token = "";
        request.getRequestData().addQueryData("token", token);
    }
}
