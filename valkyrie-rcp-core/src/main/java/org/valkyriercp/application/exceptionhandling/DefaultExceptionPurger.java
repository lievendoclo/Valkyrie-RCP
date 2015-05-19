/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.application.exceptionhandling;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * A purger that looks through to a throwable chain and can select one to unwrap.
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class DefaultExceptionPurger<SELF extends DefaultExceptionPurger<SELF>> implements ExceptionPurger {

    protected List<Class<? extends Throwable>> includeThrowableClassList = Collections.emptyList();
    protected List<Class<? extends Throwable>> excludeThrowableClassList = Collections.emptyList();

    public DefaultExceptionPurger() {}

    protected final SELF self() {
        return (SELF) this;
    }

    public DefaultExceptionPurger(Class<? extends Throwable> includeThrowableClass, Class<? extends Throwable> excludeThrowableClass) {
        if (includeThrowableClass != null) {
            setIncludeThrowableClasses(includeThrowableClass);
        }
        if (excludeThrowableClass != null) {
            setExcludeThrowableClasses(excludeThrowableClass);
        }
    }

    public DefaultExceptionPurger(List<Class<? extends Throwable>> includeThrowableClassList, List<Class<? extends Throwable>> excludeThrowableClassList) {
        if (includeThrowableClassList != null) {
            this.includeThrowableClassList = includeThrowableClassList;
        }
        if (excludeThrowableClassList != null) {
            this.excludeThrowableClassList = excludeThrowableClassList;
        }
    }

    public SELF including(Class includeThrowableClass) {
        setIncludeThrowableClasses(includeThrowableClass);
        return self();
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
    public void setIncludeThrowableClasses(Class<? extends Throwable>... includeThrowableClassList) {
        this.includeThrowableClassList = Lists.newArrayList(includeThrowableClassList);
    }

    public SELF including(Class<? extends Throwable>... throwableClasses) {
        setIncludeThrowableClasses(throwableClasses);
        return self();
    }

    /**
     * See @{link {@link #setExcludeThrowableClasses(Class...)}.
     * @param excludeThrowableClass used as a singleton list for excludeThrowableClassList
     */
    public SELF excluding(Class<? extends Throwable> excludeThrowableClass) {
        setExcludeThrowableClasses(excludeThrowableClass);
        return self();
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
     * @param throwableClasses a list of classes
     */
    public void setExcludeThrowableClasses(Class<? extends Throwable>... throwableClasses) {
        this.excludeThrowableClassList = Lists.newArrayList(throwableClasses);
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

    protected boolean containedIn(Throwable e, List<Class<? extends Throwable>> throwableClassList) {
        for (Class throwableClass : throwableClassList) {
            if (throwableClass.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

}
