package com.flylighten.app.gameheadline.event;

import org.greenrobot.eventbus.EventBus;

import in.srain.cube.app.lifecycle.LifeCycleComponentManager;

public class EventCenter {

    private static final EventBus instance = new EventBus();

    private EventCenter() {
    }

    public static CommonEventHandler bindContainerAndHandler(Object container, CommonEventHandler handler) {
        LifeCycleComponentManager.tryAddComponentToContainer(handler, container);
        return handler;
    }

    public static final EventBus inst() {
        return instance;
    }
}