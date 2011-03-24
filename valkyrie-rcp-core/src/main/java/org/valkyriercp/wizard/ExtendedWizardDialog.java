package org.valkyriercp.wizard;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.util.GuiStandardUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Wizard dialog that additionally adds a panel to the dialog
 * showing the page path and the current page.
 *
 * @author Schaubroeck N.V.
 */
public class ExtendedWizardDialog extends WizardDialog
{
    private Map<String, Component> indexComponents = new HashMap<String, Component>();
    private Map<String, Component> indexNumbers = new HashMap<String, Component>();
    private JLabel stepNofMax = new JLabel();
    private String id = null;

    public ExtendedWizardDialog()
    {
        super();
    }

    public ExtendedWizardDialog(Wizard wizard)
    {
        this(wizard, null);
    }

    public ExtendedWizardDialog(Wizard wizard, String id)
    {
        super(wizard);
        this.id = id;
        if (this.id != null)
            getApplicationConfig().applicationObjectConfigurer().configure(this, this.id);
    }

    public void setWizard(Wizard wizard)
    {
        super.setWizard(wizard);
        this.wizard = wizard;
    }

    protected JComponent createDialogContentPane()
    {
        JPanel wizardPanel = new JPanel(new FormLayout(
                new ColumnSpec[]{
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                        ColumnSpec.decode("fill:pref"),
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[]{
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        RowSpec.decode("fill:pref")
                }));
        CellConstraints cc = new CellConstraints();
        wizardPanel.add(new JSeparator(SwingConstants.VERTICAL), cc.xy(2, 1));
        wizardPanel.add(super.createDialogContentPane(), cc.xy(3, 1));
        wizardPanel.add(createWizardIndex(), cc.xy(1, 1)); // do this after super.createDialogPane() because only then, pages are added
        wizardPanel.add(new JSeparator(), cc.xyw(1, 2, 3));
        return wizardPanel;
    }

    private Component createWizardIndex()
    {
        JPanel indexPanel = new JPanel(new FormLayout(
                new ColumnSpec[]{
                        new ColumnSpec(ColumnSpec.CENTER, Sizes.DEFAULT, FormSpec.NO_GROW)
                },
                new RowSpec[]{
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                }));
        CellConstraints cc = new CellConstraints();
        GuiStandardUtils.attachBorder(indexPanel, BorderFactory.createEmptyBorder(5, 5, 5, 5));
        indexPanel.add(createWizardTitle(), cc.xy(1, 1));
        WizardPage[] pages = wizard.getPages();
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
                "right:pref, 3dlu, left:pref", ""));
        JLabel indexNumber;
        JLabel indexTitle;
        for (int i = 0; i < pages.length; ++i)
        {
            indexNumber = new JLabel(Integer.toString(i + 1) + ".");
            indexNumber.setName(Integer.toString(i + 1));
            indexTitle = new JLabel(pages[i].getTitle());
            indexNumbers.put(pages[i].getTitle(), indexNumber);
            indexComponents.put(pages[i].getTitle(), indexTitle);
            builder.append(indexNumber);
            builder.append(indexTitle);
            if (i < pages.length - 1)
                builder.nextLine();
        }
        indexPanel.add(builder.getPanel(), cc.xy(1, 3));
        indexPanel.add(createStepNofMPanel(pages.length), cc.xy(1, 5));
        return indexPanel;
    }

    /** @return  */
    private JLabel createWizardTitle()
    {
        return new JLabel(getTitle());
    }

    private Component createStepNofMPanel(int m)
    {
        JPanel panel = new JPanel(new FormLayout("fill:pref, fill:pref:grow, fill:pref", "center:pref:none"));
        CellConstraints cc = new CellConstraints();
        panel.add(new JLabel("Stap "), cc.xy(1, 1));
        panel.add(this.stepNofMax, cc.xy(2, 1));
        panel.add(new JLabel(" van " + Integer.toString(m)), cc.xy(3, 1));
        return panel;
    }

    public void showPage(WizardPage page)
    {
        JComponent component;
        String pageTitle;
        if (getCurrentPage() != null)
        {
            pageTitle = getCurrentPage().getTitle();
            component = (JComponent) indexComponents.get(pageTitle);
            component.setFont(component.getFont().deriveFont(Font.PLAIN));
            component = (JComponent) indexNumbers.get(pageTitle);
            component.setFont(component.getFont().deriveFont(Font.PLAIN));
        }
        super.showPage(page);
        pageTitle = page.getTitle();
        component = (JComponent) indexComponents.get(pageTitle);
        component.setFont(component.getFont().deriveFont(Font.BOLD));
        component = (JComponent) indexNumbers.get(pageTitle);
        component.setFont(component.getFont().deriveFont(Font.BOLD));
        this.stepNofMax.setText(component.getName());
    }
}

