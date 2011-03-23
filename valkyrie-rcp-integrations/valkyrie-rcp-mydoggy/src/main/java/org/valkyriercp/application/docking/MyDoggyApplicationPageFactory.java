package org.valkyriercp.application.docking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.application.*;

import java.io.IOException;

public class MyDoggyApplicationPageFactory implements ApplicationPageFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        MyDoggyApplicationPage page = new MyDoggyApplicationPage(window, descriptor);

        window.addPageListener(new PageListener() {

            @Override
            public void pageOpened(ApplicationPage page) {
                // normally this should be called on application start
                MyDoggyApplicationPage myDoggyApplicationPage = (MyDoggyApplicationPage) page;
                // determine wether we can load from file or
                // we should start with only one predefined page
                boolean appliedLayout = false;

                try {
                    if (myDoggyApplicationPage.loadLayout()) {
                        appliedLayout = true;
                    }
                } catch (IOException ex) {
                    logger.warn("Failed to restore layout from file!", ex);
                }

                if (!appliedLayout) {
                    myDoggyApplicationPage.buildInitialLayout();
                }
            }

            @Override
            public void pageClosed(ApplicationPage aPage) {
                // TODO this won't be called?
                // normally this should be called on exit of the application
            }
        });

        return page;
    }
}