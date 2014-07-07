package org.valkyriercp.rules.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.rules.Rules;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.constraint.ConstraintsAccessor;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.util.ClassUtils;

/**
 * A default rules source implementation which is simply a in-memory registry
 * for bean validation rules backed by a map.
 * 
 * @author Keith Donald
 */
public class DefaultRulesSource extends ConstraintsAccessor implements
		RulesSource {
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String DEFAULT_CONTEXT_ID = "default";

	private Map ruleContexts = new org.valkyriercp.util.CachingMapDecorator() {
		protected Object create(Object key) {
			return new HashMap();
		}
	};

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
		Assert.notNull(contextId);
		Assert.notNull(rules);
		Map context = getRuleContext(contextId);
		context.put(rules.getDomainObjectType(), rules);
	}

	private Map getRuleContext(String contextId) {
		return (Map) ruleContexts.get(contextId);
	}

	/**
	 * Set the list of rules retrievable by this source, where each item in the
	 * list is a <code>Rules</code> object which maintains validation rules for
	 * a bean class.
	 * 
	 * @param rules
	 *            The list of rules.
	 */
	public void setRules(List rules) {
		Assert.notNull(rules);
		if (logger.isDebugEnabled()) {
			logger.debug("Configuring rules in source...");
		}
		getRuleContext(DEFAULT_CONTEXT_ID).clear();
		for (Iterator i = rules.iterator(); i.hasNext();) {
			addRules((Rules) i.next());
		}
	}

	public Rules getRules(Class bean) {
		return getRules(bean, DEFAULT_CONTEXT_ID);
	}

	public Rules getRules(Class beanType, String contextId) {
		Assert.notNull(beanType);
		if (!StringUtils.hasText(contextId)) {
			contextId = DEFAULT_CONTEXT_ID;
		}
		return (Rules) ClassUtils.getValueFromMapForClass(beanType,
				getRuleContext(contextId));
	}

	public PropertyConstraint getPropertyConstraint(Class bean,
			String propertyName) {
		return getPropertyConstraint(bean, propertyName, DEFAULT_CONTEXT_ID);
	}

	public PropertyConstraint getPropertyConstraint(Class bean,
			String propertyName, String contextId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieving rules for bean '" + bean + "', context = "
					+ contextId + ", property '" + propertyName + "'");
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
		return new ToStringCreator(this).append("rules", ruleContexts)
				.toString();
	}

}
