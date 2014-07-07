package org.valkyriercp.application.support

import spock.lang.FailsWith
import spock.lang.Specification

import javax.swing.*

class DefaultViewDescriptorSpec extends Specification {

    def "test constructor"() {
        when:
        def descriptor = new DefaultViewDescriptor("theView", TestView.class);

        then:
        descriptor.id == "theView"
        descriptor.viewClass == TestView
    }

    def "test view creation"() {
        given:
        def descriptor = new DefaultViewDescriptor("theView", TestView.class)

        when:
        def view = descriptor.createPageComponent();

        then:
        view != null
        view instanceof TestView
    }

    def "test view creation with properties"() {
        given:
        def viewProperties = [:]
        viewProperties << ["stringProperty":"test value"]
        def descriptor = new DefaultViewDescriptor("theView", TestView.class, viewProperties);

        when:
        def view = descriptor.createPageComponent();

        then:
        view != null
        view instanceof TestView
        (view as TestView).stringProperty == "test value"
    }

    @FailsWith(IllegalArgumentException)
    def "test set view class"() {
        given:
        def descriptor = new DefaultViewDescriptor();
        descriptor.setId("viewId");
        def notAViewClass = String.class;

        expect:
        descriptor.setViewClass(notAViewClass);
    }

    static class TestView extends AbstractView {
        String stringProperty;

        protected TestView() {
            super("testView");
        }

        @Override
        protected JComponent createControl() {
            return new JLabel("test");
        }
    }
}