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
package org.valkyriercp.binding.support;

import org.junit.Test;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.test.TestBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests class {@link BeanPropertyAccessStrategy}.
 *
 * @author Oliver Hutchison
 */
public class BeanPropertyAccessStrategyTests extends AbstractPropertyAccessStrategyTests {

	protected AbstractPropertyAccessStrategy createPropertyAccessStrategy(Object target) {
		return new BeanPropertyAccessStrategy(target);
	}

    /**
     * Test the metadata on type/readability/writeability.
     */
    @Test
    public void testMetaData() {
        PropertyMetadataAccessStrategy mas = pas.getMetadataAccessStrategy();

        assertPropertyMetadata(mas, "simpleProperty", String.class, true, true);
        assertPropertyMetadata(mas, "mapProperty", Map.class, true, true);
        assertPropertyMetadata(mas, "listProperty", List.class, true, true);
        assertPropertyMetadata(mas, "readOnly", Object.class, true, false);
        assertPropertyMetadata(mas, "writeOnly", Object.class, false, true);

        // test nested property
        // when null, no type, not readable, not writeable
        assertPropertyMetadata(mas, "nestedProperty.simpleProperty", null, false, false);
        final TestBean nestedProperty = new TestBean();
        testBean.setNestedProperty(nestedProperty);
        // when provided, type/readable/writeable deducted from nested object
        assertPropertyMetadata(mas, "nestedProperty.simpleProperty", String.class, true, true);

        // test access to map
        final Map map = new HashMap();
        testBean.setMapProperty(map);
        map.put("key", new Integer(1));
        assertPropertyMetadata(mas, "mapProperty[key]", Integer.class, true, true);
    }

}