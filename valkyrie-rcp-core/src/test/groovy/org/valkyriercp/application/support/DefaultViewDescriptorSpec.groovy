/*
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