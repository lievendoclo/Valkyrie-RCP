package org.valkyriercp.core;

public interface Dirtyable {
    public static final String DIRTY_PROPERTY = "dirty";

    public boolean isDirty();
}