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
package org.valkyriercp.sample.dockingframes.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.event.LifecycleApplicationEvent;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.command.ActionCommandExecutor;
import org.valkyriercp.command.SecuredActionCommandExecutor;
import org.valkyriercp.command.support.AbstractActionCommandExecutor;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.GlobalCommandIds;
import org.valkyriercp.dialog.ConfirmationDialog;
import org.valkyriercp.list.ListSelectionValueModelAdapter;
import org.valkyriercp.list.ListSingleSelectionGuard;
import org.valkyriercp.sample.dockingframes.domain.Contact;
import org.valkyriercp.sample.dockingframes.domain.ContactDataStore;
import org.valkyriercp.util.PopupMenuMouseListener;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * This class provides the main view of the contacts. It provides a table showing the contact objects and a quick filter
 * field to narrow down the list of visible contacts. Several commands are tied to the selection of the contacts table
 * <p/>
 * By implementing special tag interfaces, this component will be automatically wired in to certain events of interest.
 * <ul>
 * <li><b>ApplicationListener</b> - This component will be automatically registered as a listener for application
 * events.</li>
 * </ul>
 *
 * @author Larry Streepy
 */
public class ContactView extends AbstractView
{
    private GlazedListTableWidget widget;

    /**
     * The data store holding all our contacts.
     */
    private ContactDataStore contactDataStore;

    /**
     * Handler for the "New Contact" action.
     */
    private ActionCommandExecutor newContactExecutor = new NewContactExecutor();

    /**
     * Handler for the "Properties" action.
     */
    private SecuredActionCommandExecutor propertiesExecutor = new PropertiesExecutor();

    /**
     * Handler for the "Delete" action.
     */
    private SecuredActionCommandExecutor deleteExecutor = new DeleteExecutor();

    /**
     * The text field allowing the user to filter the contents of the contact table.
     */
    private JTextField filterField;

    /**
     * Default constructor.
     */
    public ContactView()
    {
        super("contactView");

    }

    /**
     * @return the contactDataStore
     */
    protected ContactDataStore getContactDataStore()
    {
        return contactDataStore;
    }

    /**
     * @param contactDataStore the contactDataStore to set
     */
    public void setContactDataStore(ContactDataStore contactDataStore)
    {
        this.contactDataStore = contactDataStore;
    }

    /**
     * Create the control for this view. This method is called by the platform in order to obtain the control to add to
     * the surrounding window and page.
     *
     * @return component holding this view
     */
    protected JComponent createControl()
    {
        PropertyColumnTableDescription desc = new PropertyColumnTableDescription("contactViewTable", Contact.class);
        desc.addPropertyColumn("lastName").withMinWidth(150);
        desc.addPropertyColumn("firstName").withMinWidth(150);
        desc.addPropertyColumn("address.address1");
        desc.addPropertyColumn("address.city");
        desc.addPropertyColumn("address.state");
        desc.addPropertyColumn("address.zip");
        widget = new GlazedListTableWidget(Arrays.asList(contactDataStore.getAllContacts()), desc);
        JPanel table = new JPanel(new BorderLayout());
        table.add(widget.getListSummaryLabel(), BorderLayout.NORTH);
        table.add(widget.getComponent(), BorderLayout.CENTER);
        table.add(widget.getButtonBar(), BorderLayout.SOUTH);

        CommandGroup popup = new CommandGroup();
        popup.add((ActionCommand) getWindowCommandManager().getCommand("deleteCommand", ActionCommand.class));
        popup.addSeparator();
        popup.add((ActionCommand) getWindowCommandManager().getCommand("propertiesCommand", ActionCommand.class));
        JPopupMenu popupMenu = popup.createPopupMenu();

        widget.getTable().addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                // If the user right clicks on a row other than the selection,
                // then move the selection to the current row
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    int rowUnderMouse = widget.getTable().rowAtPoint(e.getPoint());
                    if (rowUnderMouse != -1 && !widget.getTable().isRowSelected(rowUnderMouse))
                    {
                        // Select the row under the mouse
                        widget.getTable().getSelectionModel().setSelectionInterval(rowUnderMouse, rowUnderMouse);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() >= 2)
                {
                    if (propertiesExecutor.isEnabled())
                        propertiesExecutor.execute();
                }
            }
        });

        widget.getTable().addMouseListener(new PopupMenuMouseListener(popupMenu));

        ValueModel selectionHolder = new ListSelectionValueModelAdapter(widget.getTable().getSelectionModel());
        new ListSingleSelectionGuard(selectionHolder, deleteExecutor);
        new ListSingleSelectionGuard(selectionHolder, propertiesExecutor);

        JPanel view = new JPanel(new BorderLayout());
        view.add(widget.getTextFilterField(), BorderLayout.NORTH);
        view.add(table, BorderLayout.CENTER);
        return view;
    }

    /**
     * Register the local command executors to be associated with named commands. This is called by the platform prior
     * to making the view visible.
     */
    protected void registerLocalCommandExecutors(PageComponentContext context)
    {
        context.register("newContactCommand", newContactExecutor);
        context.register(GlobalCommandIds.PROPERTIES, propertiesExecutor);
        getApplicationConfig().securityControllerManager().addSecuredObject(propertiesExecutor);
        context.register(GlobalCommandIds.DELETE, deleteExecutor);
        getApplicationConfig().securityControllerManager().addSecuredObject(deleteExecutor);
    }

    /**
     * Private inner class to create a new contact.
     */
    private class NewContactExecutor implements ActionCommandExecutor
    {
        public void execute()
        {
            new ContactPropertiesDialog(getContactDataStore()).showDialog();
        }
    }

    /**
     * Private inner class to handle the properties form display.
     */
    private class PropertiesExecutor extends AbstractActionCommandExecutor
    {
        public void execute()
        {
            for (Object selected : widget.getSelectedRows())
                new ContactPropertiesDialog((Contact) selected, getContactDataStore()).showDialog();
        }
    }

    /**
     * Private class to handle the delete command. Note that due to the configuration above, this executor is only
     * enabled when exactly one contact is selected in the table. Thus, we don't have to protect against being executed
     * with an incorrect state.
     */
    private class DeleteExecutor extends AbstractActionCommandExecutor
    {
        private DeleteExecutor() {
            setAuthorities("ADMIN");
        }

        public void execute()
        {
            String title = getApplicationConfig().messageResolver().getMessage("contact.confirmDelete.title");
            String message = getApplicationConfig().messageResolver().getMessage("contact.confirmDelete.message");
            ConfirmationDialog dlg = new ConfirmationDialog(title, message)
            {
                protected void onConfirm()
                {
                    for (Object selected : widget.getSelectedRows())
                    {
                        Contact contact = (Contact) selected;
                        // Delete the object from the persistent store.
                        getContactDataStore().delete(contact);
                        widget.removeRowObject(contact);
                        // And notify the rest of the application of the change
                        getApplicationConfig().applicationContext().publishEvent(
                                new LifecycleApplicationEvent(LifecycleApplicationEvent.DELETED, contact));
                    }
                }
            };
            dlg.showDialog();
        }
    }
}