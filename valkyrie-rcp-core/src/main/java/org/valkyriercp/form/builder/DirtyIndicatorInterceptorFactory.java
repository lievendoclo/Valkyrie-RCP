package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;

/**
 * Factory for <code>DirtyIndicatorInterceptor</code> instances.
 *
 * @author Peter De Bruycker
 */

public class DirtyIndicatorInterceptorFactory extends ConfigurableFormComponentInterceptorFactory {
    protected FormComponentInterceptor createInterceptor( FormModel formModel ) {
        return new DirtyIndicatorInterceptor( formModel );
    }
}

