/**
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
package org.valkyriercp.application.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationConfig;

/**
 * <code>ApplicationWindowFactory</code> implementation for
 * <code>DefaultApplicationWindow</code>.
 *
 * @author Peter De Bruycker
 *
 */
@Component
public class DefaultApplicationWindowFactory implements ApplicationWindowFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationWindowFactory.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    public ApplicationWindow createApplicationWindow() {
        logger.info( "Creating new DefaultApplicationWindow" );

        return new DefaultApplicationWindow(applicationConfig);
    }
}
