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
package org.valkyriercp.security;

import org.springframework.security.core.Authentication;

/**
 * Event fired when a user logs out.
 * <p>
 * The old <code>Authentication</code> token (if any) is provided as the
 * event source. If no existing <code>Authentication</code> token is
 * available, then {@link ClientSecurityEvent#NO_AUTHENTICATION} object will be used.
 *
 * @author Ben Alex
 */
public class LogoutEvent extends ClientSecurityEvent {
    public LogoutEvent(Authentication authentication) {
        super(authentication);
    }
}
