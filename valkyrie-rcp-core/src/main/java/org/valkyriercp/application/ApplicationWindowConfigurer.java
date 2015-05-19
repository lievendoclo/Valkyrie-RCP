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
package org.valkyriercp.application;

import java.awt.*;

public interface ApplicationWindowConfigurer {
    public ApplicationWindow getWindow();

    public String getTitle();

    public Image getImage();

    public Dimension getInitialSize();

    public boolean getShowMenuBar();

    public boolean getShowToolBar();

    public boolean getShowStatusBar();

    public void setTitle(String title);

    public void setImage(Image image);

    public void setInitialSize(Dimension initialSize);

    public void setShowMenuBar(boolean showMenuBar);

    public void setShowToolBar(boolean showToolBar);

    public void setShowStatusBar(boolean statusBar);

}