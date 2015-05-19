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
package org.valkyriercp.rules.constraint;

import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.closure.support.AlgorithmsAccessor;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;

import java.util.Comparator;
import java.util.Set;

public class ConstraintsAccessor  extends AlgorithmsAccessor {

    protected Constraints getConstraints() {
        return Constraints.instance();
    }

    public Constraint bind(BinaryConstraint constraint, Object parameter) {
        return getConstraints().bind(constraint, parameter);
    }

    public Constraint bind(BinaryConstraint constraint, int parameter) {
        return getConstraints().bind(constraint, parameter);
    }

    public Constraint bind(BinaryConstraint constraint, float parameter) {
        return getConstraints().bind(constraint, parameter);
    }

    public Constraint bind(BinaryConstraint constraint, double parameter) {
        return getConstraints().bind(constraint, parameter);
    }

    public Constraint bind(BinaryConstraint constraint, boolean parameter) {
        return getConstraints().bind(constraint, parameter);
    }

    public Constraint testResultOf(Closure closure, Constraint constraint) {
        return getConstraints().testResultOf(closure, constraint);
    }

    public Constraint eq(Object value) {
        return getConstraints().eq(value);
    }

    public Constraint eq(int value) {
        return getConstraints().eq(value);
    }

    public Constraint eq(Object value, Comparator comparator) {
        return getConstraints().eq(value, comparator);
    }

    public Constraint gt(Comparable value) {
        return getConstraints().gt(value);
    }

    public Constraint gt(Object value, Comparator comparator) {
        return getConstraints().gt(value, comparator);
    }

    public Constraint gt(int value) {
        return getConstraints().gt(value);
    }

    public Constraint gt(long value) {
        return getConstraints().gt(value);
    }

    public Constraint gt(float value) {
        return getConstraints().gt(value);
    }

    public Constraint gt(double value) {
        return getConstraints().gt(value);
    }

    public Constraint gte(Comparable value) {
        return getConstraints().gte(value);
    }

    public Constraint gte(Object value, Comparator comparator) {
        return getConstraints().gte(value, comparator);
    }

    public Constraint gte(int value) {
        return getConstraints().gte(value);
    }

    public Constraint gte(long value) {
        return getConstraints().gte(value);
    }

    public Constraint gte(float value) {
        return getConstraints().gte(value);
    }

    public Constraint gte(double value) {
        return getConstraints().gte(value);
    }

    public Constraint lt(Comparable value) {
        return getConstraints().lt(value);
    }

    public Constraint lt(Comparable value, Comparator comparator) {
        return getConstraints().lt(value, comparator);
    }

    public Constraint lt(int value) {
        return getConstraints().lt(value);
    }

    public Constraint lt(long value) {
        return getConstraints().lt(value);
    }

    public Constraint lt(float value) {
        return getConstraints().lt(value);
    }

    public Constraint lt(double value) {
        return getConstraints().lt(value);
    }

    public Constraint lte(Comparable value) {
        return getConstraints().lte(value);
    }

    public Constraint lte(Object value, Comparator comparator) {
        return getConstraints().lte(value, comparator);
    }

    public Constraint lte(int value) {
        return getConstraints().lte(value);
    }

    public Constraint lte(long value) {
        return getConstraints().lte(value);
    }

    public Constraint lte(float value) {
        return getConstraints().lte(value);
    }

    public Constraint lte(double value) {
        return getConstraints().lte(value);
    }

    public Constraint range(Comparable min, Comparable max) {
        return getConstraints().range(min, max);
    }

    public Constraint range(Comparable min, Comparable max, boolean inclusive) {
        return getConstraints().range(min, max, inclusive);
    }

    public Constraint range(Object min, Object max, Comparator comparator) {
        return getConstraints().range(min, max, comparator);
    }

    public Constraint range(Object min, Object max, Comparator comparator, boolean inclusive) {
        return getConstraints().range(min, max, comparator, inclusive);
    }

    public Constraint range(int min, int max) {
        return getConstraints().range(min, max);
    }

    public Constraint range(long min, long max) {
        return getConstraints().range(min, max);
    }

    public Constraint range(float min, float max) {
        return getConstraints().range(min, max);
    }

    public Constraint present() {
        return getConstraints().present();
    }

    public PropertyConstraint present(String property) {
        return getConstraints().present(property);
    }

    public Constraint ifTrue(Constraint constraint, Constraint mustAlsoBeTrue) {
        return getConstraints().ifTrue(constraint, mustAlsoBeTrue);
    }

    public Constraint ifTrueElse(Constraint constraint, Constraint mustAlsoBeTrue, Constraint elseMustAlsoBeTrue) {
        return getConstraints().ifTrue(constraint, mustAlsoBeTrue, elseMustAlsoBeTrue);
    }

    public Constraint and(Constraint constraint1, Constraint constraint2) {
        return getConstraints().and(constraint1, constraint2);
    }

    public Constraint all(Constraint[] predicates) {
        return getConstraints().all(predicates);
    }

    public And conjunction() {
        return getConstraints().conjunction();
    }

    public Constraint or(Constraint constraint1, Constraint constraint2) {
        return getConstraints().or(constraint1, constraint2);
    }

    public Constraint any(Constraint[] constraints) {
        return getConstraints().any(constraints);
    }

    public Constraint not(Constraint constraint) {
        return getConstraints().not(constraint);
    }

    public Or disjunction() {
        return getConstraints().disjunction();
    }

    public Constraint inGroup(Set group) {
        return getConstraints().inGroup(group);
    }

    public Constraint inGroup(Object[] group) {
        return getConstraints().inGroup(group);
    }

    public PropertyConstraint inGroup(String propertyName, Object[] group) {
        return getConstraints().inGroup(propertyName, group);
    }

    public Constraint like(String encodedLikeString) {
        return getConstraints().like(encodedLikeString);
    }

    public PropertyConstraint like(String property, Like.LikeType likeType, String value) {
        return getConstraints().like(property, likeType, value);
    }

    public Constraint required() {
        return getConstraints().required();
    }

    public PropertyConstraint required(String property) {
        return getConstraints().required(property);
    }

    public Constraint maxLength(int maxLength) {
        return getConstraints().maxLength(maxLength);
    }

    public Constraint minLength(int minLength) {
        return getConstraints().minLength(minLength);
    }

    public Constraint regexp(String regexp) {
        return getConstraints().regexp(regexp);
    }

    public Constraint regexp(String regexp, String constraintType) {
        return getConstraints().regexp(regexp, constraintType);
    }

    public Constraint method(Object target, String methodName, String constraintType) {
        return getConstraints().method(target, methodName, constraintType);
    }

    public PropertyConstraint value(String propertyName, Constraint valueConstraint) {
        return getConstraints().value(propertyName, valueConstraint);
    }

    public PropertyConstraint all(String propertyName, Constraint[] constraints) {
        return getConstraints().all(propertyName, constraints);
    }

    public PropertyConstraint any(String propertyName, Constraint[] constraints) {
        return getConstraints().any(propertyName, constraints);
    }

    public PropertyConstraint not(PropertyConstraint constraint) {
        return getConstraints().not(constraint);
    }

    public PropertyConstraint eq(String propertyName, Object propertyValue) {
        return getConstraints().eq(propertyName, propertyValue);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint eq(String propertyName, Object propertyValue, Comparator comparator) {
        return getConstraints().eq(propertyName, propertyValue, comparator);
    }

    public PropertyConstraint gt(String propertyName, Comparable propertyValue) {
        return getConstraints().gt(propertyName, propertyValue);
    }

    public PropertyConstraint gte(String propertyName, Comparable propertyValue) {
        return getConstraints().gte(propertyName, propertyValue);
    }

    public PropertyConstraint lt(String propertyName, Comparable propertyValue) {
        return getConstraints().lt(propertyName, propertyValue);
    }

    public PropertyConstraint lte(String propertyName, Comparable propertyValue) {
        return getConstraints().lte(propertyName, propertyValue);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint eqProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return getConstraints().eqProperty(propertyName, otherPropertyName, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint gtProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return getConstraints().gtProperty(propertyName, otherPropertyName, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint gteProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return getConstraints().gteProperty(propertyName, otherPropertyName, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint ltProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return getConstraints().ltProperty(propertyName, otherPropertyName, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint lteProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return getConstraints().lteProperty(propertyName, otherPropertyName, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint inRange(String propertyName, Comparable min, Comparable max, Comparator comparator) {
        return getConstraints().inRange(propertyName, min, max, comparator);
    }

    /**
     * @since 0.3.0
     */
    public PropertyConstraint inRangeProperties(String propertyName, String minPropertyName, String maxPropertyName, Comparator comparator) {
        return getConstraints().inRangeProperties(propertyName, minPropertyName, maxPropertyName, comparator);
    }

    public PropertyConstraint eqProperty(String propertyName, String otherPropertyName) {
        return getConstraints().eqProperty(propertyName, otherPropertyName);
    }

    public PropertyConstraint gtProperty(String propertyName, String otherPropertyName) {
        return getConstraints().gtProperty(propertyName, otherPropertyName);
    }

    public PropertyConstraint gteProperty(String propertyName, String otherPropertyName) {
        return getConstraints().gteProperty(propertyName, otherPropertyName);
    }

    public PropertyConstraint ltProperty(String propertyName, String otherPropertyName) {
        return getConstraints().ltProperty(propertyName, otherPropertyName);
    }

    public PropertyConstraint lteProperty(String propertyName, String otherPropertyName) {
        return getConstraints().lteProperty(propertyName, otherPropertyName);
    }

    public PropertyConstraint inRange(String propertyName, Comparable min, Comparable max) {
        return getConstraints().inRange(propertyName, min, max);
    }

    public PropertyConstraint inRangeProperties(String propertyName, String minPropertyName, String maxPropertyName) {
        return getConstraints().inRangeProperties(propertyName, minPropertyName, maxPropertyName);
    }

    public PropertyConstraint unique(String propertyName) {
        return getConstraints().unique(propertyName);
    }

}