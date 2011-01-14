package org.valkyriercp.form.builder;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author oliverh
 */
public class ChainedInterceptorFactory implements FormComponentInterceptorFactory {

    public List interceptorFactories = Collections.EMPTY_LIST;

    public ChainedInterceptorFactory() {
    }

    public void setInterceptorFactories(List interceptorFactories) {
        Assert.notNull(interceptorFactories);
        this.interceptorFactories = interceptorFactories;
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        List interceptors = getInterceptors(formModel);
        if (interceptors.size() == 0) {
            return null;
        }
        return new ChainedInterceptor(interceptors);
    }

    private List getInterceptors(FormModel formModel) {
        List interceptors = new ArrayList();
        for (Iterator i = interceptorFactories.iterator(); i.hasNext();) {
            FormComponentInterceptor interceptor = ((FormComponentInterceptorFactory)i.next())
                    .getInterceptor(formModel);
            if (interceptor != null) {
                interceptors.add(interceptor);
            }
        }
        return interceptors;
    }

    private static class ChainedInterceptor implements FormComponentInterceptor {
        private List interceptors;

        public ChainedInterceptor(List interceptors) {
            this.interceptors = interceptors;
        }

        public void processLabel(String propertyName, JComponent label) {
            for (Iterator i = interceptors.iterator(); i.hasNext();) {
                FormComponentInterceptor interceptor = ((FormComponentInterceptor)i.next());
                interceptor.processLabel(propertyName, label);
            }
        }

        public void processComponent(String propertyName, JComponent component) {
            for (Iterator i = interceptors.iterator(); i.hasNext();) {
                FormComponentInterceptor interceptor = ((FormComponentInterceptor)i.next());
                interceptor.processComponent(propertyName, component);
            }
        }
    }
}
