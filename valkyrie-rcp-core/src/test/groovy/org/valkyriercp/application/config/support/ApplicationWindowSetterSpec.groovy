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
package org.valkyriercp.application.config.support

import org.valkyriercp.application.ApplicationWindow
import spock.lang.FailsWith
import spock.lang.Specification

class ApplicationWindowSetterSpec extends Specification {
    @FailsWith(IllegalArgumentException)
    def "initializing with null should throw exception"() {
        expect:
        new ApplicationWindowSetter(null)
    }

    def "application window setter should set window aware property"() {
        given:
        def window = Mock(ApplicationWindow)
        def windowAware = Mock(ApplicationWindowAware)
        ApplicationWindowSetter windowSetter = new ApplicationWindowSetter(window);

        when:
        windowSetter.postProcessBeforeInitialization(null, "bogusBeanName");

        then:
        0 * windowAware.setApplicationWindow(window)

        when:
        windowSetter.postProcessBeforeInitialization(windowAware, "bogusBeanName");

        then:
        1 * windowAware.setApplicationWindow(window)
    }
}