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
package org.valkyriercp.util;

public class LabelUtils {

    public static String addElipses(String text) {
        if (text != null) {
            text += UIConstants.ELLIPSIS;
        }
        return text;
    }

    public static String removeElipses(String text) {
        if (text != null) {
            int i = text.indexOf(UIConstants.ELLIPSIS);
            if (i != -1) {
                text = text.substring(0, i);
            }
        }
        return text;
    }

    public static String htmlBlock(String labelText) {
        return "<html>" + labelText + "</html>";
    }

}