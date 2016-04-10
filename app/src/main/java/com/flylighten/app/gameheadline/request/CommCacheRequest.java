package com.flylighten.app.gameheadline.request;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestHandler;
import in.srain.cube.request.JsonData;

public class CommCacheRequest<T> extends CacheAbleRequest<T> {

    public CommCacheRequest() {
        super();
    }

    public CommCacheRequest(CacheAbleRequestHandler<T> handler) {
        super(handler);
    }

    @Override
    public void prepareRequest() {
        CommRequestManager.getInstance().prepareRequest(this);
        super.prepareRequest();
    }
}
