package org.valkyriercp.rules.reporting;

import java.util.Map;

/**
 * @TODO field error type stuff, errors interface adaption
 * @TODO number of errors per field...
 * @author  Keith Donald
 */
public interface BeanValidationResults {

	public Map getResults();

	public int getViolatedCount();

	public PropertyResults getResults(String propertyName);
}
