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
package org.valkyriercp.application.docking;

import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * <tt>ApplicationPageFactory</tt> that creates instances of <tt>DockingFramesApplicationPage</tt>.
 * 
 * @author Rogan Dawes
 */
public class DockingFramesApplicationPageFactory implements ApplicationPageFactory {
    private boolean reusePages;
    private Map<Object, Map<Object, Object>> pageCache = new HashMap<Object, Map<Object, Object>>();
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.richclient.application.ApplicationPageFactory#createApplicationPage(org.springframework.richclient.application.ApplicationWindow,
     *      org.springframework.richclient.application.PageDescriptor)
     */
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        return new DockingFramesApplicationPage(window, descriptor);
    }
}
