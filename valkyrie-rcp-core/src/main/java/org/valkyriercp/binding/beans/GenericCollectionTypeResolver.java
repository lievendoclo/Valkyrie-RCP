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

import org.springframework.core.MethodParameter;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Map;

/**
 * Helper class for determining element types of collections and maps.
 *
 * <p>Mainly intended for usage within the framework, determining the
 * target type of values to be added to a collection or map
 * (to be able to attempt type conversion if appropriate).
 *
 * <p>Only usable on Java 5. Use an appropriate JdkVersion check before
 * calling this class, if a fallback for JDK 1.3/1.4 is desirable.
 *
 * This implementation fixes some bugs of its superclass.
 *
 * @author Arne Limburg
 */
public abstract class GenericCollectionTypeResolver extends org.springframework.core.GenericCollectionTypeResolver {

   /**
    * Determine the generic element type of the given Collection class
    * (if it declares one through a generic superclass or generic interface).
    * @param collectionClass the collection class to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getCollectionType(Class collectionClass) {
       return toClass(extractType(collectionClass, Collection.class, 0));
   }

   /**
    * Determine the generic key type of the given Map class
    * (if it declares one through a generic superclass or generic interface).
    * @param mapClass the map class to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapKeyType(Class mapClass) {
       return toClass(extractType(mapClass, Map.class, 0));
   }

   /**
    * Determine the generic value type of the given Map class
    * (if it declares one through a generic superclass or generic interface).
    * @param mapClass the map class to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueType(Class mapClass) {
       return toClass(extractType(mapClass, Map.class, 1));
   }


   /**
    * Determine the generic element type of the given Collection field.
    * @param collectionField the collection field to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getCollectionFieldType(Field collectionField) {
       return getGenericFieldType(collectionField, 0, 0);
   }

   /**
    * Determine the generic element type of the given Collection field.
    * @param collectionField the collection field to introspect
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getCollectionFieldType(Field collectionField, int nestingLevel) {
       return getGenericFieldType(collectionField, 0, nestingLevel);
   }

   /**
    * Determine the generic key type of the given Map field.
    * @param mapField the map field to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapKeyFieldType(Field mapField) {
       return getGenericFieldType(mapField, 0, 0);
   }

   /**
    * Determine the generic key type of the given Map field.
    * @param mapField the map field to introspect
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapKeyFieldType(Field mapField, int nestingLevel) {
       return getGenericFieldType(mapField, 0, nestingLevel);
   }

   /**
    * Determine the generic value type of the given Map field.
    * @param mapField the map field to introspect
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueFieldType(Field mapField) {
       return getGenericFieldType(mapField, 1, 0);
   }

   /**
    * Determine the generic value type of the given Map field.
    * @param mapField the map field to introspect
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueFieldType(Field mapField, int nestingLevel) {
       return getGenericFieldType(mapField, 1, nestingLevel);
   }

   /**
    * Determine the generic element type of the given Collection parameter.
    * @param methodParam the method parameter specification
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getCollectionParameterType(MethodParameter methodParam) {
       return toClass(getGenericParameterType(methodParam, 0));
   }

   /**
    * Determine the generic key type of the given Map parameter.
    * @param methodParam the method parameter specification
    * @return the generic type, or <code>null</code> if none
    */
  public static Class getMapKeyParameterType(MethodParameter methodParam) {
       return toClass(getGenericParameterType(methodParam, 0));
   }

   /**
    * Determine the generic value type of the given Map parameter.
    * @param methodParam the method parameter specification
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueParameterType(MethodParameter methodParam) {
       return toClass(getGenericParameterType(methodParam, 1));
   }

   /**
    * Determine the generic element type of the given Collection return type.
    * @param method the method to check the return type for
    * @return the generic type, or <code>null</code> if none
   */
   public static Class getCollectionReturnType(Method method) {
       return toClass(getGenericReturnType(method, 0, 1));
   }

   /**
    * Determine the generic element type of the given Collection return type.
    * <p>If the specified nesting level is higher than 1, the element type of
    * a nested Collection/Map will be analyzed.
    * @param method the method to check the return type for
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
   */
   public static Class getCollectionReturnType(Method method, int nestingLevel) {
       return toClass(getGenericReturnType(method, 0, nestingLevel));
   }

   /**
    * Determine the generic key type of the given Map return type.
    * @param method the method to check the return type for
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapKeyReturnType(Method method) {
       return toClass(getGenericReturnType(method, 0, 1));
   }

   /**
    * Determine the generic key type of the given Map return type.
    * @param method the method to check the return type for
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapKeyReturnType(Method method, int nestingLevel) {
       return toClass(getGenericReturnType(method, 0, nestingLevel));
   }

  /**
    * Determine the generic value type of the given Map return type.
    * @param method the method to check the return type for
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueReturnType(Method method) {
       return toClass(getGenericReturnType(method, 1, 1));
   }

   /**
   * Determine the generic value type of the given Map return type.
    * @param method the method to check the return type for
    * @param nestingLevel the nesting level of the target type
    * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
    * nested List, whereas 2 would indicate the element of the nested List)
    * @return the generic type, or <code>null</code> if none
    */
   public static Class getMapValueReturnType(Method method, int nestingLevel) {
       return toClass(getGenericReturnType(method, 1, nestingLevel));
   }

   public static Class getIndexedValueFieldType(Field field) {
      return getIndexedValueFieldType(field, 1);
   }

   public static Class getIndexedValueFieldType(Field field, int nestingLevel) {
       return getGenericIndexedValueType(field.getGenericType(), nestingLevel);
   }

   public static Class getIndexedValueMethodType(MethodParameter methodParameter) {
       return getIndexedValueMethodType(methodParameter, methodParameter.getNestingLevel());
   }

   public static Class getIndexedValueMethodType(MethodParameter methodParameter, int nestingLevel) {
       Type type = null;
       Method method = methodParameter.getMethod();
       if (method != null) {
           if (methodParameter.getParameterIndex() >= 0) {
               type = method.getGenericParameterTypes()[methodParameter.getParameterIndex()];
           }
           else {
               type = method.getGenericReturnType();
           }
       } else {
           type = methodParameter.getConstructor().getGenericParameterTypes()[methodParameter.getParameterIndex()];
       }
       return getGenericIndexedValueType(type, nestingLevel);
   }

   /**
    * Extract the generic parameter type from the given method or constructor.
    * @param methodParam the method parameter specification
    * @param typeIndex the index of the type (e.g. 0 for Collections,
    * 0 for Map keys, 1 for Map values)
    * @return the generic type, or <code>null</code> if none
    */
   private static Class getGenericParameterType(MethodParameter methodParam, int typeIndex) {
       return toClass(extractType(methodParam, getTargetType(methodParam), typeIndex, methodParam.getNestingLevel()));
   }

   /**
    * Determine the target type for the given parameter specification.
    * @param methodParam the method parameter specification
    * @return the corresponding generic parameter or return type
    */
   private static Type getTargetType(MethodParameter methodParam) {
       if (methodParam.getConstructor() != null) {
           return methodParam.getConstructor().getGenericParameterTypes()[methodParam.getParameterIndex()];
       }
       else {
           if (methodParam.getParameterIndex() >= 0) {
               return methodParam.getMethod().getGenericParameterTypes()[methodParam.getParameterIndex()];
           }
           else {
               return methodParam.getMethod().getGenericReturnType();
           }
       }
   }

   /**
    * Returns the source class for the specified type.
    * This is either <code>Collection.class</code> or <code>Map.class</code>.
    * If none of both matches the baseclass of the specified type is returned.
    * @param type the type to determine the source class for
    * @return the source class
    */
   private static Class getSourceClass(Type type) {
       Class sourceClass = toClass(type);
       if (Collection.class.isAssignableFrom(sourceClass)) {
           return Collection.class;
       }
       else if (Map.class.isAssignableFrom(sourceClass)) {
           return Map.class;
       }
       else {
           return sourceClass;
       }
   }

   /**
    * Extract the generic type from the given field.
    * @param field the field to check the type for
    * @param typeIndex the index of the type (e.g. 0 for Collections,
    * 0 for Map keys, 1 for Map values)
    * @param nestingLevel the nesting level of the target type
    * @return the generic type, or <code>null</code> if none
    */
   private static Class getGenericFieldType(Field field, int typeIndex, int nestingLevel) {
       return toClass(extractType(null, field.getGenericType(), typeIndex, nestingLevel));
   }

   /**
    * Extract the generic return type from the given method.
    * @param method the method to check the return type for
    * @param typeIndex the index of the type (e.g. 0 for Collections,
    * 0 for Map keys, 1 for Map values)
    * @param nestingLevel the nesting level of the target type
    * @return the generic type, or <code>null</code> if none
    */
   private static Class getGenericReturnType(Method method, int typeIndex, int nestingLevel) {
       return toClass(extractType(null, method.getGenericReturnType(), typeIndex, nestingLevel));
   }

   private static Class getGenericIndexedValueType(Type type, int nestingLevel) {
       Class actualType = toClass(type);
       for (int i = 0; i < nestingLevel; i++) {
           if (actualType.isArray()) {
               if (type instanceof GenericArrayType) {
                   type = ((GenericArrayType)type).getGenericComponentType();
               }
               else {
                   type = actualType.getComponentType();
               }
           }
           else if (Collection.class.isAssignableFrom(actualType)) {
               type = extractType(type, Collection.class, 0);
           }
           else if (Map.class.isAssignableFrom(actualType)) {
               type = extractType(type, Map.class, 1);
           }
           actualType = toClass(type);
       }
       return actualType;
   }

   private static Type extractType(MethodParameter methodParameter, Type type, int typeIndex, int nestingLevel) {
       for (int i = 0; i <= nestingLevel; i++) {
           Integer currentTypeIndex = (methodParameter != null? methodParameter.getTypeIndexForLevel(i): null);
           int indexToUse = (currentTypeIndex != null? currentTypeIndex.intValue(): i == nestingLevel? typeIndex: getValueTypeIndex(type));
           type = extractType(type, getSourceClass(type), indexToUse);
       }
       return type;
   }

   private static int getValueTypeIndex(Type type) {
       return getSourceClass(type) == Map.class? 1: 0;
   }

   private static Type extractType(Type type, Class source, int typeIndex) {
       if (type instanceof ParameterizedType) {
           ParameterizedType parameterizedType = (ParameterizedType)type;
           Type actualType = getActualType(parameterizedType, source, typeIndex);
           if (actualType != null) {
               return actualType;
           }
           type = parameterizedType.getRawType();
       }
       if (type instanceof GenericArrayType) {
           if (typeIndex == 0) {
               return ((GenericArrayType)type).getGenericComponentType();
           } else {
               return null;
          }
       }
       if (type instanceof Class) {
           Class classType = (Class)type;
           if (classType.isArray()) {
               return classType.getComponentType();
           }
           Type returnType = extractType(classType.getGenericSuperclass(), source, typeIndex);
           if (returnType != null) {
               return returnType;
           }
           Type[] genericInterfaces = classType.getGenericInterfaces();
           for (int i = 0; i < genericInterfaces.length; i++) {
               returnType = extractType(genericInterfaces[i], source, typeIndex);
               if (returnType != null) {
                   return returnType;
               }
           }
       }
       return null;
   }

   public static Type getActualType(ParameterizedType type, Class superType, int typeIndex) {
       if (superType.equals(type.getRawType())) {
           return type.getActualTypeArguments()[typeIndex];
       }
       if (!(type.getRawType() instanceof Class)) {
           return null;
       }
       Class rawType = (Class)type.getRawType();
       Type genericSuperclass = rawType.getGenericSuperclass();
       if (genericSuperclass instanceof ParameterizedType) {
           Type actualType = getActualType((ParameterizedType)genericSuperclass, superType, typeIndex);
           if (actualType instanceof TypeVariable) {
               actualType = getActualType((TypeVariable)actualType, rawType.getTypeParameters(), type.getActualTypeArguments());
           }
           if (actualType != null) {
               return actualType;
           }
       }
       Type[] genericInterfaces = ((Class)type.getRawType()).getGenericInterfaces();
       for (int i = 0; i < genericInterfaces.length; i++) {
           if (genericInterfaces[i] instanceof ParameterizedType) {
               Type actualType = getActualType((ParameterizedType)genericInterfaces[i], superType, typeIndex);
               if (actualType instanceof TypeVariable) {
                   actualType = getActualType((TypeVariable)actualType, rawType.getTypeParameters(), type.getActualTypeArguments());
               }
               if (actualType != null) {
                   return actualType;
               }
           }
       }
       return null;
   }

   private static Type getActualType(TypeVariable typeVariable, TypeVariable[] typeVariables, Type[] actualTypeArguments) {
       if (typeVariable == null) {
           return null;
       }
       for (int i = 0; i < typeVariables.length; i++) {
           if (typeVariables[i].equals(typeVariable)) {
               return actualTypeArguments[i];
           }
       }
       return null;
   }

   public static Class toClass(Type type) {
       if (type instanceof Class) {
           return (Class)type;
       } else if (type instanceof ParameterizedType) {
           return toClass(((ParameterizedType)type).getRawType());
       } else if (type instanceof TypeVariable) {
           return toClass(((TypeVariable)type).getBounds()[0]);
       } else if (type instanceof WildcardType) {
           return toClass(((WildcardType)type).getUpperBounds()[0]);
       } else if (type instanceof GenericArrayType) {
           return Array.newInstance(toClass(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
       } else {
           return Object.class;
       }
   }
}