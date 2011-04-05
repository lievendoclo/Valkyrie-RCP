package org.valkyriercp.sample.showcase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.sample.showcase.widget.BinderDemoWidget;
import org.valkyriercp.widget.HTMLViewWidget;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetViewDescriptor;

@Configuration
public class ShowcaseViews {

    @Bean
    public WidgetViewDescriptor startView() {
        Widget htmlViewWidget = new HTMLViewWidget(new ClassPathResource("/org/valkyriercp/sample/showcase/html/start.html"));
        WidgetViewDescriptor descriptor = new WidgetViewDescriptor("startView", htmlViewWidget);
        return descriptor;
    }

    @Bean
    public WidgetViewDescriptor binderDemoView() {
        Widget widget = binderDemoWidget();
        WidgetViewDescriptor descriptor = new WidgetViewDescriptor("binderDemoView", widget);
        return descriptor;
    }

    @Bean
    public Widget binderDemoWidget() {
        return new BinderDemoWidget();
    }
}
