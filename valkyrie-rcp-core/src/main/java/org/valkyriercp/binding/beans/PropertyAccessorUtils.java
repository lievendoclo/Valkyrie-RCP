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
package org.valkyriercp.binding.beans;

import org.springframework.beans.PropertyAccessor;

/**
 * Utility methods for classes that perform bean property access
 * according to the {@link org.springframework.beans.PropertyAccessor} interface.
 *
 * @author Arne Limburg
 */
public abstract class PropertyAccessorUtils extends org.springframework.beans.PropertyAccessorUtils {

    /**
     * Returns the path to the parent component of the provided property path.
     */
   public static String getParentPropertyPath(String propertyPath) {
       int propertySeparatorIndex = getLastNestedPropertySeparatorIndex(propertyPath);
       return propertySeparatorIndex == -1? "": propertyPath.substring(0, propertySeparatorIndex);
   }

   /**
    * Returns the last component of the specified property path
    */
   public static String getPropertyName(String propertyPath) {
       int propertySeparatorIndex = PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(propertyPath);
       return propertySeparatorIndex == -1? propertyPath: propertyPath.substring(propertySeparatorIndex + 1);
   }

   /**
    * Tests whether the specified property path denotes an indexed property.
    */
   public static boolean isIndexedProperty(String propertyName) {
       return propertyName.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX_CHAR) != -1
           && propertyName.charAt(propertyName.length() - 1) == PropertyAccessor.PROPERTY_KEY_SUFFIX_CHAR;
   }

   public static boolean isNestedProperty(String propertyPath) {
       return getFirstNestedPropertySeparatorIndex(propertyPath) != -1;
   }

   public static int getNestingLevel(String propertyName) {
       propertyName = getPropertyName(propertyName);
       int nestingLevel = 0;
       boolean inKey = false;
       for (int i = 0; i < propertyName.length(); i++) {
           switch (propertyName.charAt(i)) {
           case PropertyAccessor.PROPERTY_KEY_PREFIX_CHAR:
               if (!inKey) {
                   nestingLevel++;
               }
           case PropertyAccessor.PROPERTY_KEY_SUFFIX_CHAR:
               inKey = !inKey;
           }
       }
       return nestingLevel;
   }
}