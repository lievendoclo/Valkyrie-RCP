package org.valkyriercp.application.exceptionhandling;

import java.util.Collections;
import java.util.List;

/**
 * A purger that looks through to a throwable chain and can select one to unwrap.
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class DefaultExceptionPurger implements ExceptionPurger {

    protected List<Class> includeThrowableClassList = Collections.emptyList();
    protected List<Class> excludeThrowableClassList = Collections.emptyList();

    public DefaultExceptionPurger() {}

    public DefaultExceptionPurger(Class includeThrowableClass, Class excludeThrowableClass) {
        if (includeThrowableClass != null) {
            this.includeThrowableClassList = Collections.singletonList(includeThrowableClass);
        }
        if (excludeThrowableClass != null) {
            this.excludeThrowableClassList = Collections.singletonList(excludeThrowableClass);
        }
    }

    public DefaultExceptionPurger(List<Class> includeThrowableClassList, List<Class> excludeThrowableClassList) {
        if (includeThrowableClassList != null) {
            this.includeThrowableClassList = includeThrowableClassList;
        }
        if (excludeThrowableClassList != null) {
            this.excludeThrowableClassList = excludeThrowableClassList;
        }
    }

    /**
     * See @{link {@link #setIncludeThrowableClassList(List)}.
     * @param includeThrowableClass used as a singleton list for includeThrowableClassList
     */
    public void setIncludeThrowableClass(Class includeThrowableClass) {
        setIncludeThrowableClassList(Collections.singletonList(includeThrowableClass));
    }

    /**
     * Sets Throwables that if found, are unwrapped.
     * These Throwables are ussually very specific exceptions, for example: LoginCredentialsExpiredException.
     * The earliest throwable found is selected.
     * </p>
     * Given a chain A1->B1->C1->B2->D1:
     * {A} returns A1;
     * {B} returns B1;
     * {D} returns D1;
     * {Z) returns A1;
     * {C, Z} returns C1;
     * {B, D} returns B1;
     * {D, B} return B1;
     * </p>
     * When combined, includeThrowableClassList takes priority over excludeThrowableClassList.
     * @param includeThrowableClassList a list of classes
     */
    public void setIncludeThrowableClassList(List<Class> includeThrowableClassList) {
        this.includeThrowableClassList = includeThrowableClassList;
    }

    /**
     * See @{link {@link #setExcludeThrowableClassList(List)}.
     * @param excludeThrowableClass used as a singleton list for excludeThrowableClassList
     */
    public void setExcludeThrowableClass(Class excludeThrowableClass) {
        setExcludeThrowableClassList(Collections.singletonList(excludeThrowableClass));
    }

    /**
     * Sets Throwables that if found, its cause is unwrapped.
     * These Throwables are ussually very general wrapper exceptions, for example: WrapperException.
     * The last throwable found's cause is selected.
     * If the cause is null, itself is selected.
     * </p>
     * Given a chain A1->B1->C1->B2->D1:
     * {A} returns B1;
     * {B} returns D1;
     * {D} returns D1;
     * {Z) returns A1;
     * {C, Z} returns B2;
     * {C, D} returns D1;
     * {D, C} return D1;
     * </p>
     * When combined, includeThrowableClassList takes priority over excludeThrowableClassList.
     * @param excludeThrowableClassList a list of classes
     */
    public void setExcludeThrowableClassList(List<Class> excludeThrowableClassList) {
        this.excludeThrowableClassList = excludeThrowableClassList;
    }


    public Throwable purge(Throwable root) {
        Throwable excludedPurged = root;
        Throwable e = root;
        while (e != null) {
            if (containedIn(e, includeThrowableClassList)) {
                return e;
            }
            boolean excludedContained = containedIn(e, excludeThrowableClassList);
            if (excludedContained) {
                excludedPurged = e; // in case the cause is null
            }
            // get cause of e (and null at end of chain)
            e = (e.getCause() == e) ? null : e.getCause();
            if (excludedContained && e != null) {
                excludedPurged = e; // in case the cause is not null
            }
        }
        return excludedPurged;
    }


//    public Throwable purge(Throwable root) {
//        Throwable purged = root;
//        Throwable e = root;
//        while (containedIn(e, excludeThrowableClassList)) {
//            // get cause of e (and null at end of chain)
//            e = (e.getCause() == e) ? null : e.getCause();
//            if (e == null) {
//                break;
//            }
//            purged = e;
//        }
//        return purged;
//    }

    protected boolean containedIn(Throwable e, List<Class> throwableClassList) {
        for (Class throwableClass : throwableClassList) {
            if (throwableClass.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

}
