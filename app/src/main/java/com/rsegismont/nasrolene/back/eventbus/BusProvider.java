package com.rsegismont.nasrolene.back.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Rolène on 04/02/2018.
 */

public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
