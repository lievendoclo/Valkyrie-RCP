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
package org.valkyriercp.layout;

import javax.swing.*;

/**
 * LayoutBuilders exist to make it easier to do component layouts. Typical usage
 * is to create an instance of the builder, add components to it, the call the
 * builder's {@link #getPanel()}method.
 */
public interface LayoutBuilder {

    /**
     * Creates and returns a JPanel with all the given components in it, using
     * the "hints" that were provided to the builder.
     *
     * @return a new JPanel with the components laid-out in it; never null
     */
    JPanel getPanel();

}
