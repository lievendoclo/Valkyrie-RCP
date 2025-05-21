/**
 * Copyright (C) 2015 Valkyrie RCP
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.rules.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.rules.Rules;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.constraint.ConstraintsAccessor;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.util.ClassUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A default rules source implementation which is simply a in-memory registry for bean validation rules backed by a map.
 *
 * @author Keith Donald
 */
public class DefaultRulesSource extends ConstraintsAccessor implements RulesSource {
    protected final Log logger = LogFactory.getLog(getClass());

    private static final String DEFAULT_CONTEXT_ID = "default";

    private Cache<String, Map> ruleContexts = new Cache2kBuilder<String, Map>() {
    }
            .loader(key -> new HashMap())
            .build();

    /**
     * Add or update the rules for a single bean class.
     *
     * @param rules
     *            The rules.
     */
    public void addRules(Rules rules) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding rules -> " + rules);
        }
        addRules(DEFAULT_CONTEXT_ID, rules);
    }

    public void addRules(String contextId, Rules rules) {
        Assert.notNull(contextId, "contextId should not be null");
        Assert.notNull(rules, "rules should not be null");
        Map context = getRuleContext(contextId);
        context.put(rules.getDomainObjectType(), rules);
    }

    private Map getRuleContext(String contextId) {
        return ruleContexts.get(contextId);
    }

    /**
     * Set the list of rules retrievable by this source, where each item in the list is a <code>Rules</code> object
     * which maintains validation rules for a bean class.
     *
     * @param rules
     *            The list of rules.
     */
    public void setRules(List rules) {
        Assert.notNull(rules, "rules should not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("Configuring rules in source...");
        }
        getRuleContext(DEFAULT_CONTEXT_ID).clear();
        for (Iterator i = rules.iterator(); i.hasNext(); ) {
            addRules((Rules) i.next());
        }
    }

    public Rules getRules(Class bean) {
        return getRules(bean, DEFAULT_CONTEXT_ID);
    }

    public Rules getRules(Class beanType, String contextId) {
        Assert.notNull(beanType, "beanType should not be null");
        if (!StringUtils.hasText(contextId)) {
            contextId = DEFAULT_CONTEXT_ID;
        }
        return (Rules) ClassUtils.getValueFromMapForClass(beanType, getRuleContext(contextId));
    }

    public PropertyConstraint getPropertyConstraint(Class bean, String propertyName) {
        return getPropertyConstraint(bean, propertyName, DEFAULT_CONTEXT_ID);
    }

    public PropertyConstraint getPropertyConstraint(Class bean, String propertyName, String contextId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Retrieving rules for bean '" + bean + "', context = " + contextId + ", property '"
                    + propertyName + "'");
        }
        Rules rules = getRules(bean, contextId);
        if (rules != null)
            return rules.getPropertyConstraint(propertyName);

        return null;
    }

    public static RulesSource create(Rules... rules) {
        DefaultRulesSource rulesSource = new DefaultRulesSource();
        for (Rules rule : rules) {
            rulesSource.addRules(rule);
        }
        return rulesSource;
    }

    public String toString() {
        return new ToStringCreator(this).append("rules", ruleContexts).toString();
    }

}
