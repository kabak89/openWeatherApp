package com.test.kabak.openweather.core;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Resource<T> {
    @IntDef({
            LOADING,
            COMPLETED,
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    public static final int LOADING = 0;
    public static final int COMPLETED = 1;

    @Status
    public final int status;

    @Nullable
    public final T data;

    @Nullable
    public final Exception exception;

    public Resource(@Status int status, @Nullable T data, @Nullable Exception exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }
}
