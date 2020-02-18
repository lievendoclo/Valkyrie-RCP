package org.valkyriercp.image;

import java.awt.*;
import java.io.IOException;

public interface ImageResource {
    Image getImage() throws IOException;
}
