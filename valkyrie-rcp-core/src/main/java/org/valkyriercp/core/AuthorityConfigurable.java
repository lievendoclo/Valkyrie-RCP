package org.valkyriercp.core;

public interface AuthorityConfigurable {

    void setAuthorities(String... authorities);

    String[] getAuthorities();
}
