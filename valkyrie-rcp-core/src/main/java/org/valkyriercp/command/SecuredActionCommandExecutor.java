package org.valkyriercp.command;

import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Secured;

public interface SecuredActionCommandExecutor extends GuardedActionCommandExecutor, Secured {
}
