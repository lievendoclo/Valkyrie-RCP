package org.valkyriercp.rules.closure;

/**
 * Marks the Template as being able to run a Closure on its contents.
 *
 * @author Keith Donald
 */
public interface ClosureTemplate {

	/**
	 * Execute the template with the specific closure callback for the insertion
	 * of custom processing code.
	 *
	 * @param templateCallback The procedure callback.
	 */
	void run(Closure templateCallback);

}
