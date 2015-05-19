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
package org.valkyriercp.form.binding.swing.text;

import javax.swing.text.Document;

/**
 * Adds the functionality to create a new {@link javax.swing.text.Document} to be used in a {@link javax.swing.text.JTextComponent}.
 *
 * @author Lieven Doclo
 * @author Jan Hoskens
 */
public interface DocumentFactory {

    /**
     * Create a new {@link javax.swing.text.Document} to be used in a {@link javax.swing.text.JTextComponent}.
     *
     * @return an implementation of Document.
     */
    Document createDocument();
}
