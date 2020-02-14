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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An exception handler that selects an appropriate exception handler from a list
 * based on the thrown exception and delegates the handling of the exception to it.
 * <p/>
 * This class works very similar to catch statements:
 * the first delegate which can handle the exception will handle it.
 * For example, consider 3 simple delegates for the following classes in this order:
 * NullPointerException (1), RuntimeException (2), IllegalArgumentException (3).
 * A thrown IllegalArgumentException will be handled by the (2) handler. The (3) handler is useless.
 *
 * @see ExceptionHandlerDelegate
 * @see SimpleExceptionHandlerDelegate
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class DelegatingExceptionHandler<SELF extends DelegatingExceptionHandler<SELF>> extends AbstractRegisterableExceptionHandler<SELF> implements InitializingBean {

    protected final transient Log logger = LogFactory.getLog(getClass());

    protected List<ExceptionHandlerDelegate> delegateList;
    protected ExceptionPurger exceptionPurger = null;

    /**
     * Sets the list of delegates.
     * This is not a map because the order is important
     * and delegate selection is not a simple key based selector.
     * @param delegateList a list of DelegatingExceptionHandlerDelegate
     */
    public void setDelegateList(List<ExceptionHandlerDelegate> delegateList) {
        this.delegateList = delegateList;
    }

    public SELF delegatingInOrderTo(ExceptionHandlerDelegate... delegateList) {
        setDelegateList(Arrays.asList(delegateList));
        return self();
    }

    public void addDelegateToList(ExceptionHandlerDelegate... delegateList) {
        getDelegateList().addAll(Arrays.asList(delegateList));
    }

    public List<ExceptionHandlerDelegate> getDelegateList() {
        if(delegateList == null)
            delegateList = new ArrayList<>();
        return delegateList;
    }

    /**
     * If set the throwable will first be purged before handling it.
     * @param exceptionPurger
     */
    public void setExceptionPurger(ExceptionPurger exceptionPurger) {
        this.exceptionPurger = exceptionPurger;
    }

    public SELF purgedBy(ExceptionPurger exceptionPurger) {
        setExceptionPurger(exceptionPurger);
        return self();
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(delegateList, "The delegate list must contains at least one entry.");
    }

    /**
     * Delegates the throwable to the appropriate delegate exception handler.
     * @param thread the thread in which the throwable occurred
     * @param throwable the thrown throwable
     */
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (exceptionPurger != null) {
            throwable = exceptionPurger.purge(throwable);
        }
        for (ExceptionHandlerDelegate delegate : delegateList) {
            if (delegate.hasAppropriateHandler(throwable)) {
                delegate.uncaughtException(thread, throwable);
                return;
            }
        }
        // A silent exception handler should be configured if it needs to be silent
        logger.error("No exception handler found for throwable", throwable);
    }

}

