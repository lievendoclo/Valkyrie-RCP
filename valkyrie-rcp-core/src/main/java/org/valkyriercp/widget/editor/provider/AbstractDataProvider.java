package org.valkyriercp.widget.editor.provider;

import java.util.Observable;

/**
 * Base implementation for DataProviders.
 *
 * @author Jan Hoskens
 *
 */
public abstract class AbstractDataProvider extends Observable implements DataProvider
{

    private final String id;

    public AbstractDataProvider()
    {
        this("abstractDataProvider");
    }

    public AbstractDataProvider(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    /**
     * A basic implementation that directs the necessary logic to {@link #isDetailObject(Object)} and
     * {@link #loadDetailObject(Object)}.
     */
    public final Object getDetailObject(Object selectedObject, boolean forceLoad)
    {
        if (forceLoad || !isDetailObject(selectedObject))
            return loadDetailObject(selectedObject);
        return selectedObject;
    }

    /**
     * Check if the given object is a detailed object or not. If it is already detailed, the object can be
     * returned as-is instead of loading it from the back-end.
     *
     * @param objectToCheck
     *            object to check.
     * @return <code>true</code> if the object is a detailed one.
     */
    protected boolean isDetailObject(Object objectToCheck)
    {
        return true;
    }

    /**
     * Load the detailed object from the back-end. Note that although the baseObject can be detailed, you MUST
     * fetch the object from the back-end in any case in this method.
     *
     * @param baseObject
     *            object containing enough information to fetch a detailed object from the back-end.
     * @return the detailed object retrieved from the back-end.
     */
    protected Object loadDetailObject(Object baseObject)
    {
        throw new UnsupportedOperationException("getDetailObject(object) not implemented for " + baseObject);
    }


    public Object clone(Object sampleData)
    {
        throw new UnsupportedOperationException("clone(object) not implemented for " + sampleData);
    }

    public final Object update(Object updatedData)
    {
        setChanged();
        Object newEntity = doUpdate(updatedData);
        notifyObservers(DataProviderEvent.updateEntityEvent(updatedData, newEntity));
        return newEntity;
    }

    public Object doUpdate(Object updatedData)
    {
        throw new UnsupportedOperationException("doUpdate(object) not implemented for " + updatedData);
    };


    public final void delete(Object dataToRemove)
    {
        setChanged();
        doDelete(dataToRemove);
        notifyObservers(DataProviderEvent.deleteEntityEvent(dataToRemove));
    }

    public void doDelete(Object dataToRemove)
    {
        throw new UnsupportedOperationException("doDelete(object) not implemented for " + dataToRemove);
    }

    public final Object create(Object newData)
    {
        setChanged();
        Object newEntity = doCreate(newData);
        notifyObservers(DataProviderEvent.newEntityEvent(newEntity));
        return newEntity;
    }

    public Object doCreate(Object newData)
    {
        throw new UnsupportedOperationException("doCreate(object) not implemented for " + newData);
    }

    public Object newInstance(Object criteria)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void addDataProviderListener(DataProviderListener dataProviderListener)
    {
        addObserver(dataProviderListener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeDataProviderListener(DataProviderListener dataProviderListener)
    {
        deleteObserver(dataProviderListener);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsBaseCriteria()
    {
        return false;
    }

    public void setBaseCriteria(Object criteria)
    {
        throw new UnsupportedOperationException("setBaseCriteria(object) not implemented for " + criteria);
    }

    public boolean exists(Object data)
    {
        return false;
    }

    public RefreshPolicy getRefreshPolicy()
    {
        return RefreshPolicy.ON_USER_SWITCH;
    }

    public Object getSimpleObject(Object baseObject)
    {
        return baseObject;
    }
}
