package org.valkyriercp.security;

import org.springframework.security.core.Authentication;
import org.valkyriercp.rules.PropertyConstraintProvider;
import org.valkyriercp.rules.Rules;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * This class provides a bean suitable for use in a login form, providing properties for
 * storing the user name and password.
 * <p>
 * This bean provides basic constraints for the username and password properties. Each is
 * required to be at least 2 characters long. If you need more specific constraints, then
 * you should implement a subtype and override the initRules method.
 *
 * @author Larry Streepy
 * @author Ben Alex
 *
 */
public class LoginDetails implements PropertyConstraintProvider {

    public static final String PROPERTY_USERNAME = "username";

    public static final String PROPERTY_PASSWORD = "password";

    private String username;

    private String password;

    private Rules validationRules;

    public LoginDetails() {
        init();
    }

    public void init() {
        Authentication authentication = getApplicationSecurityManager().getAuthentication();
        if( authentication != null ) {
            setUsername( authentication.getName() );
        }
        initRules();
    }

    public ApplicationSecurityManager getApplicationSecurityManager() {
        return ValkyrieRepository.getInstance().getApplicationConfig().applicationSecurityManager();
    }

    /**
     * Initialize the field constraints for our properties. Minimal constraints are
     * enforced here. If you need more control, you should override this in a subtype.
     */
    protected void initRules() {
        this.validationRules = new Rules( getClass() ) {
            protected void initRules() {
                add( PROPERTY_USERNAME, all( new Constraint[] { required(), minLength( getUsernameMinLength() ) } ) );
                add( PROPERTY_PASSWORD, all( new Constraint[] { required(), minLength( getPasswordMinLength() ) } ) );
            }

            protected int getUsernameMinLength() {
                return 2;
            }

            protected int getPasswordMinLength() {
                return 2;
            }

        };
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the property constraints.
     */
    public PropertyConstraint getPropertyConstraint(String propertyName) {
        return validationRules.getPropertyConstraint( propertyName );
    }

}
