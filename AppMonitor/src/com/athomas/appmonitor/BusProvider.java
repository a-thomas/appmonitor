package com.athomas.appmonitor;

import com.squareup.otto.Bus;

public class BusProvider {

    private static Bus INSTANCE;

    public static Bus get() {

        if (INSTANCE == null) {
            INSTANCE = new Bus();
        }

        return INSTANCE;
    }

}
