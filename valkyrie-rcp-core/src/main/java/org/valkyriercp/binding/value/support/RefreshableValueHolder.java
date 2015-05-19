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
package org.valkyriercp.binding.value.support;

import org.springframework.core.style.StylerUtils;
import org.springframework.util.Assert;
import org.valkyriercp.rules.closure.Closure;

/**
 * ValueModel adding a <code>refreshable</code> aspect. A {@link Closure} is
 * given that may be executed using the {@link #refresh()} method or forced to
 * be executed on every {@link #getValue()}. The given refresh Closure is used
 * to set the value before reading it.
 * 
 * @author Keith Donald
 */
public class RefreshableValueHolder extends ValueHolder {
	private final Closure refreshFunction;

	private boolean alwaysRefresh;

    private boolean initialized = false;

	/**
	 * Constructor supplying a refresh <code>Closure</code>. Refresh has to be
	 * triggered manually.
	 */
	public RefreshableValueHolder(Closure refreshFunction) {
		this(refreshFunction, false);
	}

	/**
	 * Constructor supplying a refresh <code>Closure</code> that allways has to
	 * be triggered when reading the value.
	 */
	public RefreshableValueHolder(Closure refreshFunction, boolean alwaysRefresh) {
		this(refreshFunction, alwaysRefresh, true);
	}

	/**
	 * Constructor supplying a refresh <code>Closure</code> that allways has to
	 * be triggered when reading the value. Additionally a refresh is triggered
	 * on construction.
     *
     * @deprecated lazy initialization is always done
	 */
    @Deprecated
	public RefreshableValueHolder(Closure refreshFunction,
			boolean alwaysRefresh, boolean lazyInit) {
		super();
		Assert.notNull(refreshFunction, "The refresh callback cannot be null");
		this.refreshFunction = refreshFunction;
		this.alwaysRefresh = alwaysRefresh;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * If allwaysRefresh is set, this method will trigger refresh() each time it
	 * is executed.
	 */
	public Object getValue() {
		if (alwaysRefresh || !initialized) {
			refresh();
            initialized = true;
		}
		return super.getValue();
	}

	/**
	 * Refresh te value by executing the refresh <code>Closure</code>.
	 */
	public void refresh() {
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing held value '"
					+ StylerUtils.style(super.getValue()) + "'");
		}
		setValue(refreshFunction.call(null));
	}
}
