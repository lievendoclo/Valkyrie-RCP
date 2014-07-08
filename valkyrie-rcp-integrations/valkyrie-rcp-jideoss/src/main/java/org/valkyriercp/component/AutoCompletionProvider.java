package org.valkyriercp.component;

import java.util.List;

public interface AutoCompletionProvider {
    List getAutoCompletionOptions(String formModelId, String propertyName);
}
