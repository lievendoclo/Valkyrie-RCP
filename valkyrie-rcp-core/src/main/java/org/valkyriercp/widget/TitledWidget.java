package org.valkyriercp.widget;

import org.springframework.beans.factory.BeanNameAware;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.image.config.ImageConfigurable;

/**
 * Provides the basic {@link Widget} facilities in combination with a fully configurable title/message
 * component.
 */
public interface TitledWidget
        extends
        Widget,
        Guarded,
        Messagable,
        TitleConfigurable,
        ImageConfigurable,
        DescriptionConfigurable,
        BeanNameAware
{
    //ValidationResultsReporter newSingleLineResultsReporter(Messagable messagable);

    String getId();

}
