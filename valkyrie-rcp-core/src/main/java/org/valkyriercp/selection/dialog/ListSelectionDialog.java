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
package org.valkyriercp.selection.dialog;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.DefaultEventListModel;
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
public class ListSelectionDialog<E> extends AbstractSelectionDialog {

	private ListCellRenderer<E> renderer;

	private JList<E> list;

	private EventList<E> items;

	public ListSelectionDialog(String title, List<E> items) {
		this(title, null, GlazedLists.eventList(items));
	}

	public ListSelectionDialog(String title, Window parent, List<E> items) {
		this(title, parent, GlazedLists.eventList(items));
	}

	public ListSelectionDialog(String title, Window parent, EventList<E> items) {
		super(title, parent);
		this.items = items;
	}

	public void setRenderer(ListCellRenderer<E> renderer) {
		Assert.notNull(renderer, "Renderer cannot be null.");
		Assert.isTrue(!isControlCreated(),
				"Install the renderer before the control is created.");

		this.renderer = renderer;
	}

	protected JComponent createSelectionComponent() {
		list = getApplicationConfig().componentFactory().createList();
		list.setModel(new DefaultEventListModel<>(items));

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

	protected final JList<E> getList() {
		return list;
	}

	public EventList<E> getItems() {
		return items;
	}

}
