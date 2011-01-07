package org.valkyriercp.factory;

import org.jdesktop.swingx.JXTitledSeparator;
import org.valkyriercp.util.Alignment;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * A factory interface for encapsulating logic to create well-formed, configured
 * GUI controls.
 *
 * @author Keith Donald
 */
public interface ComponentFactory {

	/**
	 * Create and configure a label with the specified label key. For example:
	 * "&My Control Label:", where the '&' marks a positional mnemonic.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The configured label.
	 */
	public JLabel createLabel(String labelKey);

	/**
	 * Create and configure a label with the specified label key. For example:
	 * "&My Control Label:", where the '&' marks a positional mnemonic.
	 *
	 * @param labelKeys The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The configured label.
	 */
	public JLabel createLabel(String[] labelKeys);

	/**
	 * Creates and configure a label with the specified label key and
	 * parameterized arguments. Argument values are resolved to {digit
	 * placeholder} characters in the resolved message string.
	 *
	 * @param labelKey
	 * @param arguments
	 * @return The configured label.
	 */
	public JLabel createLabel(String labelKey, Object[] arguments);

	/**
	 * Create and configure a title label with the specified label key. A title
	 * label's text matches that of a titled border title (bold, highlighted.)
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 *
	 * @return The configured label.
	 */
	public JLabel createTitleLabel(String labelKey);

	/**
	 * Creates a titled border for the specified component.
	 *
	 * @param labelKey the title label message code.
	 * @param comp the component to attach a titled border to.
	 * @return the configured component.
	 */
	public JComponent createTitledBorderFor(String labelKey, JComponent comp);

	/**
	 * Create and configure a label for the provided component. Associating a
	 * label with a component ensures when the mnemonic is selected the
	 * component is given focus.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @param comp the labeled component
	 * @return The configured label.
	 */
	public JLabel createLabelFor(String labelKey, JComponent comp);

	/**
	 * Create and configure a label for the provided component. Associating a
	 * label with a component ensures when the mnemonic is selected the
	 * component is given focus.
	 *
	 * @param labelKeys The label message code; may also be the label text if no
	 * message source is configured.
	 * @param comp the labeled component
	 * @return The configured label.
	 */
	public JLabel createLabelFor(String[] labelKeys, JComponent comp);

	/**
	 * Create and configure a button with the specified label key. The button
	 * will be configured with the appropriate mnemonic and accelerator. Note:
	 * if you find yourself duplicating the same handler logic accross different
	 * buttons, maybe its time to use a Command.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The configured button.
	 */
	public JButton createButton(String labelKey);

	/**
	 * Create and configure an left-aligned label acting as a form dividing
	 * separator; that is, a control that displays a label and a separator
	 * immediately underneath it.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The configured labeled separator.
	 */
	public JXTitledSeparator createLabeledSeparator(String labelKey);

	/**
	 * Create and configure an aligned label acting as a form dividing
	 * separator; that is, a control that displays a label and a separator
	 * immediately underneath it.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @param alignment The label's alignment.
	 * @return The configured labeled separator.
	 */
	public JXTitledSeparator createLabeledSeparator(String labelKey, Alignment alignment);

	/**
	 * Create a list using this component factory.
	 *
	 * @return The new list.
	 */
	public JList createList();

	/**
	 * Create a combo box using this component factory.
	 *
	 * @return The new combo box.
	 */
	public JComboBox createComboBox();

	/**
	 * Create a configured menu item.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The menu item.
	 */
	public JMenuItem createMenuItem(String labelKey);

	/**
	 * Create a configured checkbox.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The checkbox.
	 */
	public JCheckBox createCheckBox(String labelKey);

	/**
	 * Create a configured checkbox.
	 *
	 * @param labelKeys The label message codes; may also be the label text if
	 * no message source is configured.
	 * @return The checkbox.
	 */
	public JCheckBox createCheckBox(String[] labelKeys);

	/**
	 * Create a configured toggle button.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The toggle button.
	 */
	public JToggleButton createToggleButton(String labelKey);

	/**
	 * Create a configured toggle button.
	 *
	 * @param labelKeys The label message codes; may also be the label text if
	 * no message source is configured.
	 * @return The toggle button.
	 */
	public JToggleButton createToggleButton(String[] labelKeys);

	/**
	 * Create a configured radio button.
	 *
	 * @param labelKey The label message code; may also be the label text if no
	 * message source is configured.
	 * @return The radio button.
	 */
	public JRadioButton createRadioButton(String labelKey);

	/**
	 * Create a configured radio button.
	 *
	 * @param labelKeys The label message codes; may also be the label text if
	 * no message source is configured.
	 * @return The radio button.
	 */
	public JRadioButton createRadioButton(String[] labelKeys);

	/**
	 * Create a formatted text field using this component factory.
	 *
	 * @param formatterFactory AbstractFormatterFactory used for formatting.
	 * @return The new formatted text field
	 */
	public JFormattedTextField createFormattedTextField(JFormattedTextField.AbstractFormatterFactory formatterFactory);

	/**
	 * Create a standard text field using this component factory.
	 *
	 * @return the new text field.
	 */
	public JTextField createTextField();

	/**
	 * Create a standard password field using this component factory.
	 *
	 * @return the new password field.
	 */
	public JPasswordField createPasswordField();

	/**
	 * Create a text area using this component factory.
	 *
	 * @return The new text area.
	 */
	public JTextArea createTextArea();

	/**
	 * Create a text area using this component factory.
	 *
	 * @return The new text area.
	 */
	public JTextArea createTextArea(int row, int columns);

	/**
	 * Create a text area that looks like a label (but with cut/copy/paste
	 * enabled!) using this component factory.
	 *
	 * @return The new text area.
	 */
	public JTextArea createTextAreaAsLabel();

	/**
	 * Create and return a new tabbed pane.
	 *
	 * @return a new tabbed pane.
	 */
	public JTabbedPane createTabbedPane();

	/**
	 * Adds a tab to the provided tabbed pane, configuring the tab's appearance
	 * from information retrieved using the <code>labelKey</code> property.
	 * The tab title text, icon, mnemonic, and mnemonic index are all
	 * configurable.
	 *
	 * @param tabbedPane
	 * @param labelKey
	 * @param tabComponent
	 */
	public void addConfiguredTab(JTabbedPane tabbedPane, String labelKey, JComponent tabComponent);

	/**
	 * Create a scroll pane using this component factory.
	 *
	 * @return empty scroll pane.
	 * @see javax.swing.JScrollPane#JScrollPane()
	 */
	public JScrollPane createScrollPane();

	/**
	 * Create a scroll pane using this component factory, with the specified
	 * component as the viewport view.
	 *
	 * @param view the component to display in the scrollpane's viewport
	 * @return scroll pane with specified view
	 * @see JScrollPane#JScrollPane(java.awt.Component)
	 */
	public JScrollPane createScrollPane(Component view);

	/**
	 * Create a scroll pane using this component factory, with the specified
	 * component as the viewport view and with the specified vertical and
	 * horizontal scrollbar policies.
	 *
	 * @param view the component to display in the scrollpane's viewport
	 * @param vsbPolicy set the vertical scrollbar policy.
	 * @param hsbPolicy set the horizontal scrollbar policy.
	 * @return scroll pane with specified view and scrolling policies
	 * @see JScrollPane#JScrollPane(java.awt.Component, int, int)
	 */
	public JScrollPane createScrollPane(Component view, int vsbPolicy, int hsbPolicy);

	/**
	 * Creates a panel using this component factory.
	 *
	 * @return the panel
	 * @see JPanel
	 */
	public JPanel createPanel();

	/**
	 * Creates a panel with the supplied LayoutManager using this component
	 * factory.
	 *
	 * @param layoutManager the LayoutManager that will be used by the returned
	 * panel
	 * @return a panel
	 * @see JPanel#JPanel(java.awt.LayoutManager)
	 */
	public JPanel createPanel(LayoutManager layoutManager);

	/**
	 * Construct a JTable with a default model.
	 * @return new table instance
	 */
	public JTable createTable();

	/**
	 * Construct a JTable with the specified table model.
	 * @param model TableModel to install into the new table
	 * @return new table instance
	 */
	public JTable createTable(TableModel model);

	/**
	 * Construct a JToolBar.
	 * @return new toolbar instance
	 */
	public JComponent createToolBar();

}
