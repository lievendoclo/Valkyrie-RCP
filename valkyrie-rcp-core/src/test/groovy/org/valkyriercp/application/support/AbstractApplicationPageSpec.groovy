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

import org.gcontracts.annotations.Invariant
import org.valkyriercp.AbstractValkyrieSpec
import org.valkyriercp.application.ApplicationPage

import javax.swing.*

import static org.junit.Assert.*

@Invariant({ createApplicationPage() != null})
abstract class AbstractApplicationPageSpec extends AbstractValkyrieSpec {
    private AbstractApplicationPage applicationPage;
    private TestView testView1;
    private TestView testView2;

    def setup() {
        setUpViews();
        applicationPage = (AbstractApplicationPage) createApplicationPage();
        def viewDescriptorRegistry = new SimpleViewDescriptorRegistry();
        viewDescriptorRegistry.addViewDescriptor(new SimpleViewDescriptor("testView1", testView1));
        viewDescriptorRegistry.addViewDescriptor(new SimpleViewDescriptor("testView2", testView2));
        applicationPage.setViewDescriptorRegistry(viewDescriptorRegistry);
        applicationPage.setPageComponentPaneFactory(new SimplePageComponentPaneFactory());
        applicationPage.setDescriptor(new EmptyPageDescriptor());
        JComponent control = applicationPage.getControl();
        assertNotNull("getControl cannot return null", control);
    }

    private void setUpViews() {
        testView1 = new TestView("this is test view 1");
        testView2 = new TestView("this is test view 2");
    }

    protected abstract ApplicationPage createApplicationPage();

    def "test show view and close"() {
        when:
        def view = applicationPage.getView("testView1")
        then:
        view == null

        when:
        view = applicationPage.showView("testView1");
        then:
        view != null
        view instanceof TestView
        view.id == "testView1"

        when:
        applicationPage.close(view);
        view = applicationPage.getView("testView1")
        then:
        view == null
    }

    def "test show view with input"() {
        given:
        Object input = "the input"

        when:
        applicationPage.showView("testView1", input)
        def view = applicationPage.getView("testView1") as TestView
        then:
        view != null
        view.isSetInputCalled()
        view.input == input
    }

    def "test show view"() {
        when:
        def view = applicationPage.showView("testView1")
        then:
        view == testView1
        applicationPage.activeComponent == testView1

        when:
        view = applicationPage.showView("testView2")
        then:
        view == testView2
        applicationPage.activeComponent == testView2
    }

    def "test show view without input"() {
        when:
        applicationPage.showView("testView1");
        def view = applicationPage.getView("testView1") as TestView;
        then:
        view != null
        !view.isSetInputCalled()
    }

    private static class TestView extends AbstractView {

        private String label;
        private Object input;
        private boolean setInputCalled;

        public TestView(String label) {
            super("testView");
            this.label = label;
        }

        @Override
        protected JComponent createControl() {
            return new JLabel(label);
        }

        @Override
        public void setInput(Object input) {
            this.input = input;
            setInputCalled = true;
        }

        public Object getInput() {
            return input;
        }

        public boolean isSetInputCalled() {
            return setInputCalled;
        }

    }
}