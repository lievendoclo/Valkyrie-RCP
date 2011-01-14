package org.valkyriercp.util;

import java.util.Observable;

public class ValueMonitor extends Observable
{
    private Object value;

    public void setValue(final Object newValue)
    {
        if (value != newValue)
        {
            setChanged();
            this.value = newValue;
            notifyObservers(value);
        }
    }
}