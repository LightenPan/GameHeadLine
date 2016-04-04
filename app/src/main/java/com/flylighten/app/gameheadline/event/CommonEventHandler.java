package com.flylighten.app.gameheadline.event;

import in.srain.cube.app.lifecycle.LifeCycleComponent;

public class CommonEventHandler implements LifeCycleComponent {

    private boolean mRegistered = false;

    public CommonEventHandler register() {
        if (!mRegistered) {
            mRegistered = true;
            EventCenter.inst().register(this);
        }
        return this;
    }

    public synchronized CommonEventHandler tryToUnregister() {
        if (mRegistered) {
            mRegistered = false;
            EventCenter.inst().unregister(this);
        }
        return this;
    }

    public synchronized CommonEventHandler tryToRegisterIfNot() {
        register();
        return this;
    }

    @Override
    public void onBecomesVisibleFromTotallyInvisible() {
    }

    @Override
    public void onBecomesPartiallyInvisible() {
    }

    @Override
    public void onBecomesVisible() {
        register();
    }

    @Override
    public void onBecomesTotallyInvisible() {
    }

    @Override
    public void onDestroy() {
        tryToUnregister();
    }
}