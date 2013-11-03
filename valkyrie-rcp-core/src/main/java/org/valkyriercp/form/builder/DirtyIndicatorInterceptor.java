package org.valkyriercp.form.builder;

import org.springframework.context.MessageSource;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.factory.AbstractControlFactory;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.util.OverlayHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

/**
 * Adds a "dirty overlay" to a component that is triggered by user editing. The
 * overlaid image is retrieved by the image key "dirty.overlay". The image is
 * placed at the top-left corner of the component, and the image's tooltip is
 * set to a message (retrieved with key "dirty.message") such as "{field} has
 * changed, original value was {value}.". It also adds a small revert button
 * that resets the value of the field.
 *
 * @author Peter De Bruycker
 */
public class DirtyIndicatorInterceptor extends AbstractFormComponentInterceptor {
	private static final String DIRTY_ICON_KEY = "dirty.overlay";

	private static final String DIRTY_MESSAGE_KEY = "dirty.message";

	private static final String REVERT_ICON_KEY = "revert.overlay";

	private static final String REVERT_MESSAGE_KEY = "revert.message";

	public DirtyIndicatorInterceptor(FormModel formModel) {
		super(formModel);
	}

	public void processComponent(final String propertyName, final JComponent component) {
		final OriginalValueHolder originalValueHolder = new OriginalValueHolder();
		final DirtyOverlay overlay = new DirtyOverlay(getFormModel(), propertyName, originalValueHolder);

		final ValueHolder reset = new ValueHolder(Boolean.FALSE);
		getFormModel().getValueModel(propertyName).addValueChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (reset.getValue() == Boolean.TRUE) {
					originalValueHolder.reset();
					reset.setValue(Boolean.FALSE);

					overlay.setVisible(false);

					return;
				}

				if (!originalValueHolder.isInitialized()) {
					originalValueHolder.setOriginalValue(evt.getOldValue());
				}

				Object oldValue = originalValueHolder.getValue();
				Object newValue = evt.getNewValue();
				overlay.setVisible(getValueChangeDetector().hasValueChanged(oldValue, newValue)
                    && !getFormModel().getFieldMetadata(propertyName).isReadOnly());
			}
		});
		getFormModel().getFormObjectHolder().addValueChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				// reset original value, new "original" value is in the form
				// model as the form object has changed
				reset.setValue(Boolean.TRUE);
			}
		});

		InterceptorOverlayHelper.attachOverlay(overlay.getControl(), component, OverlayHelper.NORTH_WEST, 5, 0);
		overlay.setVisible(false);
	}

	private ValueChangeDetector getValueChangeDetector() {
		return getApplicationConfig().valueChangeDetector();
	}

	private static class DirtyOverlay extends AbstractControlFactory {
		private JButton revertButton;

		private JLabel dirtyLabel;

		private FormModel formModel;

		private String propertyName;

		private OriginalValueHolder originalValueHolder;

		public DirtyOverlay(FormModel formModel, String propertyName, OriginalValueHolder originalValueHolder) {
			this.formModel = formModel;
			this.propertyName = propertyName;
			this.originalValueHolder = originalValueHolder;
		}

		protected JComponent createControl() {
			final JPanel control = new JPanel(new BorderLayout()) {
				public void repaint() {
					// hack for RCP-426: if the form component is on a tabbed
					// pane, when switching between tabs when the overlay is
					// visible, the overlay is not correctly repainted. When we
					// trigger a revalidate here, everything is ok.
					revalidate();

					super.repaint();
				}
			};

			control.setName("dirtyOverlay");

			control.setOpaque(false);

			IconSource iconSource = getApplicationConfig().iconSource();
			Icon icon = iconSource.getIcon(DIRTY_ICON_KEY);
			dirtyLabel = new JLabel(icon);
			control.add(dirtyLabel, BorderLayout.CENTER);

			createRevertButton();
			control.add(revertButton, BorderLayout.LINE_END);

			return control;
		}

		private void createRevertButton() {
			IconSource iconSource = getApplicationConfig().iconSource();
			Icon icon = iconSource.getIcon(REVERT_ICON_KEY);

			revertButton = new JButton(icon);
			revertButton.setBorderPainted(false);
			revertButton.setContentAreaFilled(false);
			revertButton.setFocusable(false);
			revertButton.setMargin(new Insets(-3, -3, -3, -3));
			revertButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// reset
					formModel.getValueModel(propertyName).setValue(originalValueHolder.getValue());
				}
			});
		}

		public void setVisible(boolean visible) {
			getControl().setVisible(visible);
			// manually set the size, otherwise sometimes the overlay is not
			// shown (it has size 0,0)
			getControl().setSize(getControl().getPreferredSize());

			if (visible) {
				MessageSource messageSource = getApplicationConfig().messageSource();
				String dirtyTooltip = messageSource.getMessage(DIRTY_MESSAGE_KEY, new Object[] {
						formModel.getFieldFace(propertyName).getDisplayName(), originalValueHolder.getValue() }, Locale
						.getDefault());
				dirtyLabel.setToolTipText(dirtyTooltip);

				String revertTooltip = messageSource.getMessage(REVERT_MESSAGE_KEY, new Object[] { formModel
						.getFieldFace(propertyName).getDisplayName() }, Locale.getDefault());
				revertButton.setToolTipText(revertTooltip);
			}
		}
	}

	private static class OriginalValueHolder {
		private boolean initialized;

		private Object originalValue;

		public void setOriginalValue(Object value) {
			initialized = true;
			originalValue = value;
		}

		public void reset() {
			initialized = false;
			originalValue = null;
		}

		public Object getValue() {
			return originalValue;
		}

		public boolean isInitialized() {
			return initialized;
		}
	}
}
