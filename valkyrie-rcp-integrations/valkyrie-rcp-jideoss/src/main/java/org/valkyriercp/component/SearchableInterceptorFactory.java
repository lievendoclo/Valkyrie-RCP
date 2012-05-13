package org.valkyriercp.component;


import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

/**
 * Factory that creates {@link SearchableInterceptor}.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio ArgÃ¼ello (JAF)</a>
 */
public class SearchableInterceptorFactory implements FormComponentInterceptorFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public FormComponentInterceptor getInterceptor(FormModel formModel) {

        return SearchableInterceptor.getInstance();
    }

}
