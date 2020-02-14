package org.valkyriercp.security;

import java.util.Map;

public interface AuthorityMapContributor {
    Map<String, String> getContributedAuthorities();
}
