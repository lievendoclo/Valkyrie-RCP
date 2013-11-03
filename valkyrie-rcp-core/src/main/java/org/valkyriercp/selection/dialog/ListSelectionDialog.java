package org.valkyriercp.selection.dialog;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.EventListModel;
import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A <code>ListSelectionDialog</code> can be used to select an item from a list.
 * 
 * @author Peter De Bruycker
 */
public class ListSelectionDialog extends AbstractSelectionDialog {

	private ListCellRenderer renderer;

	private JList list;

	private EventList items;

	public ListSelectionDialog(String title, List items) {
		this(title, null, GlazedLists.eventList(items));
	}

	public ListSelectionDialog(String title, Window parent, List items) {
		this(title, parent, GlazedLists.eventList(items));
	}

	public ListSelectionDialog(String title, Window parent, EventList items) {
		super(title, parent);
		this.items = items;
	}

	public void setRenderer(ListCellRenderer renderer) {
		Assert.notNull(renderer, "Renderer cannot be null.");
		Assert.isTrue(!isControlCreated(),
				"Install the renderer before the control is created.");

		this.renderer = renderer;
	}

	protected JComponent createSelectionComponent() {
		list = getApplicationConfig().componentFactory().createList();
		list.setModel(new EventListModel(items));

		list.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		list.addListSelectionListener(new ListSelectionListener() {

			private int lastIndex = -1;

			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}

				if (list.getSelectionModel().isSelectionEmpty()
						&& lastIndex > -1) {
					if (list.getModel().getSize() > 0) {
						list.setSelectedIndex(lastIndex);
						return;
					}
				}

				setFinishEnabled(!list.getSelectionModel().isSelectionEmpty());
				lastIndex = list.getSelectedIndex();
			}
		});

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					getFinishCommand().execute();
				}
			}
		});

		if (renderer != null) {
			list.setCellRenderer(renderer);
		}

		setFinishEnabled(false);

		if (!items.isEmpty()) {
			list.setSelectedIndex(0);
		}

		return new JScrollPane(list);
	}

	protected Object getSelectedObject() {
		return items.get(list.getSelectedIndex());
	}

	protected final JList getList() {
		return list;
	}

	public EventList getItems() {
		return items;
	}

}
