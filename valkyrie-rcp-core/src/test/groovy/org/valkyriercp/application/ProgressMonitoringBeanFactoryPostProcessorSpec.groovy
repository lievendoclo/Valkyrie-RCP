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
package org.valkyriercp.application

import org.springframework.context.support.StaticApplicationContext
import org.valkyriercp.AbstractValkyrieSpec
import org.valkyriercp.application.support.ProgressMonitoringBeanFactoryPostProcessor
import org.valkyriercp.progress.NullProgressMonitor
import org.valkyriercp.progress.ProgressMonitor
import spock.lang.FailsWith
import spock.lang.Specification

class ProgressMonitoringBeanFactoryPostProcessorSpec extends Specification {

    @FailsWith(IllegalArgumentException)
    def "initializing with null should throw exception"() {
        expect:
        new ProgressMonitoringBeanFactoryPostProcessor(null)
    }

    def "correct initialization should work"() {
        expect:
        new ProgressMonitoringBeanFactoryPostProcessor(new NullProgressMonitor())
    }

    def "loading of beans should trigger progress monitor"() {
        given:
        String beanName1 = "beanName1";
        String beanName2 = "beanName2";
        String beanName3 = "beanName3";
        StaticApplicationContext appCtx = new StaticApplicationContext();
        appCtx.registerSingleton(beanName1, Object.class);
        appCtx.registerSingleton(beanName2, Object.class);
        appCtx.registerPrototype(beanName3, Object.class);
        def mockProgressMonitor = Mock(ProgressMonitor)

        when:
        ProgressMonitoringBeanFactoryPostProcessor processor = new ProgressMonitoringBeanFactoryPostProcessor(
                mockProgressMonitor);
        appCtx.addBeanFactoryPostProcessor(processor);
        appCtx.refresh();

        then:
        1 * mockProgressMonitor.taskStarted("Loading Application Context ...", 2);
        1 * mockProgressMonitor.subTaskStarted("Loading " + beanName1 + " ...");
        1 * mockProgressMonitor.worked(1);
        1 * mockProgressMonitor.subTaskStarted("Loading " + beanName2 + " ...");
        1 * mockProgressMonitor.worked(1);
        0 * _
    }
}