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

import org.junit.Test;
import org.valkyriercp.rules.closure.Closure;

import java.util.Collections;

public class RefreshableValueModelTests {
    @Test
    public void constructorShouldWork() {
        RefreshableValueHolder refreshableAssetTypeValueHolder = new RefreshableValueHolder(
                new Closure() {
                    @Override
                    public Object call(Object object) {
                        return Collections.EMPTY_LIST;
                    }
                }, true, false);
    }
}
