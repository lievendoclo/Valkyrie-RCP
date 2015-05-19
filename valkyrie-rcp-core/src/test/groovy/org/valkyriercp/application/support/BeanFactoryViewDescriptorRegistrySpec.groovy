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

import org.springframework.beans.MutablePropertyValues
import org.springframework.beans.factory.BeanNotOfRequiredTypeException
import org.springframework.context.support.StaticApplicationContext
import spock.lang.FailsWith
import spock.lang.Specification

import javax.swing.*

class BeanFactoryViewDescriptorRegistrySpec extends Specification {

    @FailsWith(BeanNotOfRequiredTypeException)
    def testGetViewDescriptor() {
        given:
        def registry = new BeanFactoryViewDescriptorRegistry();
        def appCtx = new StaticApplicationContext()
        registry.setApplicationContext(appCtx)
        def mpv = new MutablePropertyValues()
        mpv.addPropertyValue("viewClass", NullView.class)
        appCtx.registerSingleton("view1", DefaultViewDescriptor.class, mpv)
        appCtx.registerSingleton("view2", DefaultViewDescriptor.class, mpv)
        appCtx.registerSingleton("bogusView", String.class)

        expect:
        registry.getViewDescriptor("view1") != null
        registry.getViewDescriptor("view2") != null
        registry.getViewDescriptor("bogus") == null
        registry.getViewDescriptor("bogusView")

    }

    def testGetViewDescriptorsEmpty() {
        given:
        def registry = new BeanFactoryViewDescriptorRegistry();
        def appCtx = new StaticApplicationContext();
        registry.setApplicationContext(appCtx);
        def viewDescriptors = registry.getViewDescriptors();
        expect:
        viewDescriptors != null
        viewDescriptors.length == 0
    }

    def testGetViewDescriptors() {
        given:
        def registry = new BeanFactoryViewDescriptorRegistry();
        def appCtx = new StaticApplicationContext();
        registry.setApplicationContext(appCtx);
        def mpv = new MutablePropertyValues();
        mpv.addPropertyValue("viewClass", NullView.class);
        appCtx.registerSingleton("view1", DefaultViewDescriptor.class, mpv);
        appCtx.registerSingleton("view2", DefaultViewDescriptor.class, mpv);
        def viewDescriptors = registry.getViewDescriptors();
        expect:
        viewDescriptors.length == 2
    }

    @FailsWith(IllegalArgumentException)
    def testForNullViewId() {
        expect:
        new BeanFactoryViewDescriptorRegistry().getViewDescriptor(null);
    }

    private class NullView extends AbstractView
    {
        protected NullView() {
            super("nullView");
        }
        protected JComponent createControl()
        {
            return new JPanel();
        }
    }
}