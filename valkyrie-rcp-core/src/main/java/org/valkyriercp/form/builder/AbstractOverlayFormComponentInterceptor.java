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
package org.valkyriercp.form.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FieldMetadata;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractOverlayFormComponentInterceptor extends AbstractFormComponentInterceptor {

    /**
     * The "ancestor" property name.
     */
    private static final String ANCESTOR_PROPERTY = "ancestor";

    /**
     * The overlay service.
     */
    @Autowired
    private OverlayService overlayService;

    /**
     * Creates the interceptor given the target form model.
     *
     * @param formModel
     *            the form model.
     */
    public AbstractOverlayFormComponentInterceptor(FormModel formModel) {

        super(formModel);
    }

    /**
     * Gets the overlay service.
     *
     * @return the overlay service.
     */
    public final OverlayService getOverlayService() {
        if(overlayService == null)
            return ValkyrieRepository.getInstance().getApplicationConfig().overlayService();
        return this.overlayService;
    }

    /**
     * Sets the overlay service.
     *
     * @param overlayService
     *            the overlay service to set.
     */
    public final void setOverlayService(OverlayService overlayService) {

        Assert.notNull(overlayService, "overlayService");

        this.overlayService = overlayService;
    }

    /**
     * Creates an overlay handler for the given property name and component and installs the overlay.
     *
     * @param propertyName
     *            the property name.
     *
     * @param component
     *            the component.
     *
     * @see OverlayService#installOverlay(JComponent, JComponent)
     */
    @Override
    public final void processComponent(String propertyName, final JComponent component) {

        final AbstractOverlayHandler overlayHandler = this.createOverlayHandler(propertyName, component);

        // Wait until has parent and overlay is correctly installed
        final PropertyChangeListener wait4ParentListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {

                final JComponent targetComponent = overlayHandler.getTargetComponent();
                final JComponent overlay = overlayHandler.getOverlay();

                // Install overlay
                final int position = AbstractOverlayFormComponentInterceptor.this.getPosition();
                final Boolean success = AbstractOverlayFormComponentInterceptor.this.//
                        getOverlayService().installOverlay(targetComponent, overlay, position, null);

                if (success) {
                    targetComponent.removePropertyChangeListener(//
                            AbstractOverlayFormComponentInterceptor.ANCESTOR_PROPERTY, this);
                }
            }
        };

        component.addPropertyChangeListener(//
                AbstractOverlayFormComponentInterceptor.ANCESTOR_PROPERTY, wait4ParentListener);
    }

    /**
     * Gets the position where overlay should be installed.
     * <p>
     * Note installing more than one overlays at the same position will be confused.
     *
     * @return the position.
     */
    protected abstract int getPosition();

    /**
     * Creates the overlay handler for the given property name and component.
     *
     * @param propertyName
     *            the property name.
     * @param component
     *            the component.
     * @return the overlay handler.
     */
    protected abstract AbstractOverlayHandler createOverlayHandler(String propertyName, final JComponent component);

    /**
     * An overlay handler that knows the name, component and form model of the target property.
     */
    protected abstract class AbstractOverlayHandler {

        /**
         * The property name.
         */
        private String propertyName;

        /**
         * The target component.
         */
        private JComponent targetComponent;

        /**
         * The overlay component.
         */
        private JComponent overlay;

        /**
         * Creates the handler given the target component.
         *
         * @param propertyName
         *            the property name.
         *
         * @param targetComponent
         *            the target component.
         */
        public AbstractOverlayHandler(String propertyName, JComponent targetComponent) {

            this.setPropertyName(propertyName);
            this.setTargetComponent(targetComponent);
        }

        /**
         * Shows or hides the overlay.
         *
         * @param show
         *            whether to show or hide the overlay.
         *
         * @return <code>true</code> if success and <code>false</code> in other case.
         */
        protected final Boolean refreshOverlay(Boolean show) {

            if (show) {
                return this.showOverlay();
            } else {
                return this.hideOverlay();
            }
        }

        /**
         * Shows the overlay.
         *
         * @return <code>true</code> if success and <code>false</code> in other case.
         */
        protected Boolean showOverlay() {

            this.getOverlay().setSize(this.getOverlay().getPreferredSize());

            return AbstractOverlayFormComponentInterceptor.this.getOverlayService().showOverlay(//
                    this.getTargetComponent(), this.getOverlay());
        }

        /**
         * Hides the overlay.
         *
         * @return <code>true</code> if success and <code>false</code> in other case.
         */
        protected Boolean hideOverlay() {

            return AbstractOverlayFormComponentInterceptor.this.getOverlayService().hideOverlay(//
                    this.getTargetComponent(), this.getOverlay());
        }

        /**
         * Gets the form model.
         *
         * @return the form model.
         */
        protected FormModel getFormModel() {

            return AbstractOverlayFormComponentInterceptor.this.getFormModel();
        }

        /**
         * Gets the value model.
         *
         * @return the value model.
         */
        protected ValueModel getValueModel() {

            return this.getFormModel().getValueModel(this.getPropertyName());
        }

        /**
         * Gets the field metada.
         *
         * @return the field metadata.
         */
        protected FieldMetadata getFieldMetadata() {

            return this.getFormModel().getFieldMetadata(this.getPropertyName());
        }

        /**
         * Gets the property name.
         *
         * @return the property name.
         */
        protected final String getPropertyName() {

            return this.propertyName;
        }

        /**
         * Gets the target component.
         *
         * @return the target component.
         */
        protected final JComponent getTargetComponent() {

            return this.targetComponent;
        }

        /**
         * Gets the overlay component and if not exists then create it.
         *
         * @return the overlay.
         *
         * @see #createOverlay()
         */
        protected final JComponent getOverlay() {

            if (this.overlay == null) {
                this.setOverlay(this.createOverlay());
            }

            return this.overlay;
        }

        /**
         * Creates the overlay component.
         *
         * @return the overlay.
         */
        protected abstract JComponent createOverlay();

        /**
         * Sets the propertyName.
         *
         * @param propertyName
         *            the propertyName to set.
         */
        private void setPropertyName(String propertyName) {

            Assert.notNull(propertyName, "propertyName");

            this.propertyName = propertyName;
        }

        /**
         * Sets the target component.
         *
         * @param targetComponent
         *            the target component to set.
         */
        private void setTargetComponent(JComponent targetComponent) {

            Assert.notNull(targetComponent, "targetComponent");

            this.targetComponent = targetComponent;
        }

        /**
         * Sets the overlay.
         *
         * @param overlay
         *            the overlay to set.
         */
        private void setOverlay(JComponent overlay) {

            Assert.notNull(overlay, "overlay");

            this.overlay = overlay;
        }
    }
}