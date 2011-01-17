package org.valkyriercp.core;

public interface Secured extends Authorizable, AuthorityConfigurable {
    /**
     * Set the Id of the security controller that should manage this object.
     * @param controllerId Id (bean name) of the security controller
     */
    public void setSecurityControllerId(String controllerId);

    /**
     * Get the id (bean name) of the security controller that should manage this object.
     * @return controller id
     */
    public String getSecurityControllerId();

}
