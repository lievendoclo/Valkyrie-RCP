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
