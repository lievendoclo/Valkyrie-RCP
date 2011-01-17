package org.valkyriercp.widget.editor.provider;

import java.util.List;

/**
 * <p>
 * The DataProvider regulates access to the back-end services. It can be used by eg a
 * {@link org.springframework.richclient.widget.editor.DefaultDataEditorWidget} as a central access point to the services that provide the data.
 * </p>
 */
public interface DataProvider extends DataProviderEventSource
{

    /**
     * <p>
     * Each DataProvider can specify a policy that dictates when to refresh the data list. This policy must be
     * taken into account by any class using the DataProvider in order to keep the data in a consistent state.
     * </p>
     *
     * <ul>
     * <li><em>NEVER</em> No automatic refresh, user should trigger a refresh when needed.</li>
     * <li><em>ON_EMPTY</em> Fetch the data once. This usually means when your client-side data list is
     * empty.</li>
     * <li><em>ON_USER_SWITCH</em> Data needs to be refreshed when a user switch is detected. The data may
     * contain user specific entries.</li>
     * <li><em>ALLWAYS</em> Data needs to be refreshed whenever the user views the list (on switching to
     * that screen).</li>
     * </ul>
     */
    public static enum RefreshPolicy {
        NEVER, ON_EMPTY, ON_USER_SWITCH, ALLWAYS
    }

    /**
     * Fetch the detailed object from the back-end. To optimize your code, a flag is passed (forceLoad) to
     * specify if a back-end load is absolutely necessary or not. If forceLoad is <code>false</code> you may
     * return the selectedObject directly if it is already detailed. On the other hand if forceLoad is
     * <code>true</code> you MUST fetch the detailed object from the back-end. If your object doesn't have a
     * specific detail the same logic must be applied to allow the fetching of a fresh individual object.
     *
     * @param selectedObject
     *            the object that must be used to fetch the detailed one.
     * @param forceLoad
     *            if <code>true</code> always go to back-end and load the object; if <code>false</code> a
     *            shortcut can be used to return an already detailed selected object.
     */
    public Object getDetailObject(Object selectedObject, boolean forceLoad);

    public Object getSimpleObject(Object selectedObject);

    public boolean supportsFiltering();

    public List getList(Object criteria);

    public boolean supportsUpdate();

    public Object update(Object updatedData);

    public boolean supportsCreate();

    public Object create(Object newData);

    public Object newInstance(Object criteria);

    public boolean supportsClone();

    public Object clone(Object sampleData);

    public boolean supportsDelete();

    public void delete(Object dataToRemove);

    public boolean supportsBaseCriteria();

    public void setBaseCriteria(Object criteria);

    public boolean exists(Object data);

    public RefreshPolicy getRefreshPolicy();

}
