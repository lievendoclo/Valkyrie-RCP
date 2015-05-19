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
package org.valkyriercp.component;

/**
 * Interface that enables a component to set the focus on a default
 * component
 *
 * @author Jan Hoskens
 */
public interface Focussable
{
    /**
     * Set the focus on a default component
     *
     * On implementation, remember to use EventQueue.invokeLater(Runnable).
     *
     * @see java.awt.EventQueue#invokeLater(java.lang.Runnable)
     */
    public void grabFocus();
}

