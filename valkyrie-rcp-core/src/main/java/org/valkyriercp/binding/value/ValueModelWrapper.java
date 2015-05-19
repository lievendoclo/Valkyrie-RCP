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
package org.valkyriercp.binding.value;

/**
 * A value model that wraps another value model, possibly adding additional
 * functionality such as type conversion or buffering.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public interface ValueModelWrapper {

	/**
	 * Returns the <code>ValueModel</code> wrapped by this
	 * <code>ValueModelWrapper</code>.
	 *
	 * @return the wrapped value model.
	 */
	ValueModel getWrappedValueModel();

	/**
	 * Returns the inner most <code>ValueModel</code> wrapped by this
	 * <code>ValueModelWrapper</code>.
	 *
	 * @return the inner most wrapped value model.
	 */
	ValueModel getInnerMostWrappedValueModel();
}
