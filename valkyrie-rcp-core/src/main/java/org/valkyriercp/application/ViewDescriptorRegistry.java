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

/**
 * A registry for {@link ViewDescriptor} definitions.
 *
 * @author Keith Donald
 *
 */
public interface ViewDescriptorRegistry {

    /**
     * Returns an array of all the view descriptors in the registry.
     *
     * @return An array of all the view descriptors in the registry. The array may be empty but
     * will never be null.
     */
    public ViewDescriptor[] getViewDescriptors();

    /**
     * Returns the view descriptor with the given identifier, or null if no such descriptor
     * exists in the registry.
     *
     * @param viewDescriptorId The id of the view descriptor to be returned.
     * @return The view descriptor with the given id, or null.
     *
     * @throws IllegalArgumentException if {@code viewDescriptorId} is null.
     */
    public ViewDescriptor getViewDescriptor(String viewDescriptorId);

}
