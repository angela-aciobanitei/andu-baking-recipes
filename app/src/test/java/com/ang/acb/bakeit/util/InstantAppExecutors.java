package com.ang.acb.bakeit.util;

import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.concurrent.Executor;

public class InstantAppExecutors extends AppExecutors {

    private static Executor instant = Runnable::run;

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}

