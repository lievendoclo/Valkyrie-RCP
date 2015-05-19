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
package org.valkyriercp.rules.reporting;

/**
 * A interface for rendering bean properties.  For example, a
 * SpacedBeanPropertyRenderer might render the property
 * 'source.address.netmask' like 'Source Address Netmask'
 *
 * @author  Keith Donald
 */
public interface BeanPropertyNameRenderer {

	public String renderQualifiedName(String qualifiedName);

	public String renderShortName(String shortName);
}