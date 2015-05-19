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
package org.valkyriercp.application;

public interface PageComponentListener {

    /**
     * Notifies this listener that the given component has been created.
     *
     * @param component
     *            the component that was created
     */
    public void componentOpened(PageComponent component);

    /**
     * Notifies this listener that the given component has been given focus
     *
     * @param component
     *            the component that was given focus
     */
    public void componentFocusGained(PageComponent component);

    /**
     * Notifies this listener that the given component has lost focus.
     *
     * @param component
     *            the component that lost focus
     */
    public void componentFocusLost(PageComponent component);

    /**
     * Notifies this listener that the given component has been closed.
     *
     * @param component
     *            the component that was closed
     */
    public void componentClosed(PageComponent component);

}

