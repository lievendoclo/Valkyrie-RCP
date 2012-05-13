package org.valkyriercp.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.CommitTriggerListener;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.support.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Helper class that decorates a <code>JTextComponent</code> with a standard
 * popup menu. Support for undo/redo is also provided.
 *
 * @author Oliver Hutchison
 */
@Configurable(preConstruction = true)
public class TextComponentPopup extends MouseAdapter implements FocusListener, CaretListener, UndoableEditListener {

	/**
	 * Delay in ms between updates of the paste commands status. We only update
	 * the paste command's status occasionally as this is a quite expensive
	 * operation.
	 */
	private static final int PAST_REFRESH_TIMER_DELAY = 100;

	private static final String[] COMMANDS = new String[] { GlobalCommandIds.UNDO, GlobalCommandIds.REDO,
			GlobalCommandIds.COPY, GlobalCommandIds.CUT, GlobalCommandIds.PASTE, GlobalCommandIds.SELECT_ALL };

	public static void attachPopup(JTextComponent textComponent, CommitTrigger resetUndoHistoryTrigger) {
		new TextComponentPopup(textComponent, resetUndoHistoryTrigger);
	}

	public static void attachPopup(JTextComponent textComponent) {
		new TextComponentPopup(textComponent, null);
	}

	private final JTextComponent textComponent;

	private final Timer updatePasteStatusTimer;

	private final UndoManager undoManager = new UndoManager();

	private final CommitTrigger resetUndoHistoryTrigger;

	private static CommandManager localCommandManager;

	private final UndoCommandExecutor undo = new UndoCommandExecutor();

	private final RedoCommandExecutor redo = new RedoCommandExecutor();

	private final CutCommandExecutor cut = new CutCommandExecutor();

	private final CopyCommandExecutor copy = new CopyCommandExecutor();

	private final PasteCommandExecutor paste = new PasteCommandExecutor();

	private final SelectAllCommandExecutor selectAll = new SelectAllCommandExecutor();

    @Autowired
    private ApplicationConfig applicationConfig;

	protected TextComponentPopup(JTextComponent textComponent, CommitTrigger resetUndoHistoryTrigger) {
		this.textComponent = textComponent;
		this.resetUndoHistoryTrigger = resetUndoHistoryTrigger;
		this.updatePasteStatusTimer = new Timer(PAST_REFRESH_TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePasteStatus();
			}
		});
		updatePasteStatusTimer.setCoalesce(true);
		updatePasteStatusTimer.setRepeats(false);
		updatePasteStatusTimer.setInitialDelay(PAST_REFRESH_TIMER_DELAY);
		registerListeners();
		registerAccelerators();
	}

	private void registerListeners() {
		textComponent.addMouseListener(this);
		textComponent.addFocusListener(this);
		textComponent.addCaretListener(this);
		textComponent.getDocument().addUndoableEditListener(this);
		if (resetUndoHistoryTrigger != null) {
			CommitTriggerListener resetUndoHistoryHandler = new CommitTriggerListener() {
				public void commit() {
					undoManager.discardAllEdits();
					updateUndoRedoState();
				}

				public void revert() {
				}
			};
			resetUndoHistoryTrigger.addCommitTriggerListener(resetUndoHistoryHandler);
		}
	}

	protected CommandManager getCommandManager() {
		CommandManager commandManager;
		ApplicationWindow appWindow = applicationConfig.windowManager().getActiveWindow();
		if (appWindow == null || appWindow.getCommandManager() == null) {
			if (localCommandManager == null) {
				localCommandManager = new DefaultCommandManager();
			}
			commandManager = localCommandManager;
		}
		else {
			commandManager = appWindow.getCommandManager();
		}
		for (int i = 0; i < COMMANDS.length; i++) {
			if (!commandManager.containsActionCommand(COMMANDS[i])) {
				commandManager.registerCommand(new TargetableActionCommand(COMMANDS[i], null));
			}
		}
		return commandManager;
	}

	public void registerAccelerators() {
		CommandManager commandManager = getCommandManager();
		Keymap keymap = new DefaultKeymap(getClass().getName(), textComponent.getKeymap());
		for (int i = 0; i < COMMANDS.length; i++) {
			ActionCommand command = commandManager.getActionCommand(COMMANDS[i]);
			keymap.addActionForKeyStroke(command.getAccelerator(), command.getActionAdapter());
		}
		if (COMMANDS.length > 0) {
			textComponent.setKeymap(keymap);
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent evt) {
		maybeShowPopup(evt);
	}

	/**
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent evt) {
		maybeShowPopup(evt);
	}

	private void maybeShowPopup(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			updatePasteStatusNow();
			createPopup().show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	public void caretUpdate(CaretEvent e) {
		updateState();
	}

	public void focusGained(FocusEvent e) {
		updateState();
		registerCommandExecutors();
	}

	public void focusLost(FocusEvent e) {
		if (!e.isTemporary()) {
			unregisterCommandExecutors();
		}
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		undoManager.addEdit(e.getEdit());
		updateUndoRedoState();
	}

	private JPopupMenu createPopup() {
		if (textComponent instanceof JPasswordField)
			return getPasswordCommandGroup().createPopupMenu();

		if (isEditable())
			return getEditableCommandGroup().createPopupMenu();

		return getReadOnlyCommandGroup().createPopupMenu();
	}

	private void updateState() {
		boolean hasSelection = textComponent.getSelectionStart() != textComponent.getSelectionEnd();
		copy.setEnabled(hasSelection);
		selectAll.setEnabled(textComponent.getDocument().getLength() > 0);
		boolean isEditable = isEditable();
		cut.setEnabled(hasSelection && isEditable);
		if (isEditable) {
			scheduleUpdatePasteStatus();
		}
		else {
			paste.setEnabled(false);
		}
		updateUndoRedoState();
	}

	private void updateUndoRedoState() {
		undo.setEnabled(undoManager.canUndo());
		redo.setEnabled(undoManager.canRedo());
	}

	private void scheduleUpdatePasteStatus() {
		// we do this using a timer as the method canPasteFromClipboard()
		// can be a schedule significant bottle neck when there's lots of typing
		// going on
		if (!updatePasteStatusTimer.isRunning()) {
			updatePasteStatusTimer.restart();
		}
	}

	private void updatePasteStatusNow() {
		if (updatePasteStatusTimer.isRunning()) {
			updatePasteStatusTimer.stop();
		}
		updatePasteStatus();
	}

	private void updatePasteStatus() {
		paste.setEnabled(isEditable() && canPasteFromClipboard());
	}

	/**
	 * Try not to call this method to much as SystemClipboard#getContents()
	 * relatively slow.
	 */
	private boolean canPasteFromClipboard() {
		try {
			return textComponent.getTransferHandler().canImport(
					textComponent,
					Toolkit.getDefaultToolkit().getSystemClipboard().getContents(textComponent)
							.getTransferDataFlavors());
		}
		catch (IllegalStateException e) {
			/*
			 * as the javadoc of Clipboard.getContents state: the
			 * IllegalStateException can be thrown when the clipboard is not
			 * available (i.e. in use by another application), so we return
			 * false.
			 */
			return false;
		}
	}

	private boolean isEditable() {
		return !(textComponent instanceof JPasswordField) && textComponent.isEnabled() && textComponent.isEditable();
	}

	protected CommandGroup getEditableCommandGroup() {
		CommandGroup editGroup = getCommandManager().getCommandGroup("textEditMenu");
		if (editGroup == null) {
			editGroup = getCommandManager().createCommandGroup(
					"textEditMenu",
					new Object[] { GlobalCommandIds.UNDO, GlobalCommandIds.REDO, "separator", GlobalCommandIds.CUT,
							GlobalCommandIds.COPY, GlobalCommandIds.PASTE, "separator", GlobalCommandIds.SELECT_ALL });
		}
		return editGroup;
	}

	protected CommandGroup getPasswordCommandGroup() {
		CommandGroup passwordGroup = getCommandManager().getCommandGroup("passwordTextEditMenu");
		if (passwordGroup == null) {
			passwordGroup = getCommandManager().createCommandGroup("passwordTextEditMenu",
					new Object[] { GlobalCommandIds.UNDO, GlobalCommandIds.REDO });
		}
		return passwordGroup;
	}

	protected CommandGroup getReadOnlyCommandGroup() {
		CommandGroup readOnlyGroup = getCommandManager().getCommandGroup("readOnlyTextEditMenu");
		if (readOnlyGroup == null) {
			readOnlyGroup = getCommandManager().createCommandGroup("readOnlyTextEditMenu",
					new Object[] { GlobalCommandIds.COPY, "separator", GlobalCommandIds.SELECT_ALL });
		}
		return readOnlyGroup;
	}

	private void registerCommandExecutors() {
		CommandManager commandManager = getCommandManager();
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.UNDO, undo);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.REDO, redo);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.CUT, cut);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.COPY, copy);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.PASTE, paste);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.SELECT_ALL, selectAll);
	}

	private void unregisterCommandExecutors() {
		CommandManager commandManager = getCommandManager();
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.UNDO, null);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.REDO, null);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.CUT, null);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.COPY, null);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.PASTE, null);
		commandManager.setTargetableActionCommandExecutor(GlobalCommandIds.SELECT_ALL, null);
	}

	private class UndoCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			undoManager.undo();
		}
	}

	private class RedoCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			undoManager.redo();
		}
	}

	private class CutCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			textComponent.cut();
		}
	}

	private class CopyCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			textComponent.copy();
		}
	}

	private class PasteCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			textComponent.paste();
		}
	}

	private class SelectAllCommandExecutor extends AbstractActionCommandExecutor {
		public void execute() {
			textComponent.selectAll();
		}
	}

	/**
	 * We need this class since keymaps are shared in jvm This class is a 100%
	 * copy of the jdk class {@link JTextComponent#DEFAULT_KEYMAP
	 */
	public static class DefaultKeymap implements Keymap {

		String nm;

		Keymap parent;

		Hashtable bindings;

		Action defaultAction;

		DefaultKeymap(String nm, Keymap parent) {
			this.nm = nm;
			this.parent = parent;
			bindings = new Hashtable();
		}

		/**
		 * Fetch the default action to fire if a key is typed (ie a KEY_TYPED
		 * KeyEvent is received) and there is no binding for it. Typically this
		 * would be some action that inserts text so that the keymap doesn't
		 * require an action for each possible key.
		 */
		public Action getDefaultAction() {
			if (defaultAction != null) {
				return defaultAction;
			}
			return (parent != null) ? parent.getDefaultAction() : null;
		}

		/**
		 * Set the default action to fire if a key is typed.
		 */
		public void setDefaultAction(Action a) {
			defaultAction = a;
		}

		public String getName() {
			return nm;
		}

		public Action getAction(KeyStroke key) {
			Action a = (Action) bindings.get(key);
			if ((a == null) && (parent != null)) {
				a = parent.getAction(key);
			}
			return a;
		}

		public KeyStroke[] getBoundKeyStrokes() {
			KeyStroke[] keys = new KeyStroke[bindings.size()];
			int i = 0;
			for (Enumeration e = bindings.keys(); e.hasMoreElements();) {
				keys[i++] = (KeyStroke) e.nextElement();
			}
			return keys;
		}

		public Action[] getBoundActions() {
			Action[] actions = new Action[bindings.size()];
			int i = 0;
			for (Enumeration e = bindings.elements(); e.hasMoreElements();) {
				actions[i++] = (Action) e.nextElement();
			}
			return actions;
		}

		public KeyStroke[] getKeyStrokesForAction(Action a) {
			if (a == null) {
				return null;
			}
			KeyStroke[] retValue = null;
			// Determine local bindings first.
			Vector keyStrokes = null;
			for (Enumeration enum_ = bindings.keys(); enum_.hasMoreElements();) {
				Object key = enum_.nextElement();
				if (bindings.get(key) == a) {
					if (keyStrokes == null) {
						keyStrokes = new Vector();
					}
					keyStrokes.addElement(key);
				}
			}
			// See if the parent has any.
			if (parent != null) {
				KeyStroke[] pStrokes = parent.getKeyStrokesForAction(a);
				if (pStrokes != null) {
					// Remove any bindings defined in the parent that
					// are locally defined.
					int rCount = 0;
					for (int counter = pStrokes.length - 1; counter >= 0; counter--) {
						if (isLocallyDefined(pStrokes[counter])) {
							pStrokes[counter] = null;
							rCount++;
						}
					}
					if (rCount > 0 && rCount < pStrokes.length) {
						if (keyStrokes == null) {
							keyStrokes = new Vector();
						}
						for (int counter = pStrokes.length - 1; counter >= 0; counter--) {
							if (pStrokes[counter] != null) {
								keyStrokes.addElement(pStrokes[counter]);
							}
						}
					}
					else if (rCount == 0) {
						if (keyStrokes == null) {
							retValue = pStrokes;
						}
						else {
							retValue = new KeyStroke[keyStrokes.size() + pStrokes.length];
							keyStrokes.copyInto(retValue);
							System.arraycopy(pStrokes, 0, retValue, keyStrokes.size(), pStrokes.length);
							keyStrokes = null;
						}
					}
				}
			}
			if (keyStrokes != null) {
				retValue = new KeyStroke[keyStrokes.size()];
				keyStrokes.copyInto(retValue);
			}
			return retValue;
		}

		public boolean isLocallyDefined(KeyStroke key) {
			return bindings.containsKey(key);
		}

		public void addActionForKeyStroke(KeyStroke key, Action a) {
			bindings.put(key, a);
		}

		public void removeKeyStrokeBinding(KeyStroke key) {
			bindings.remove(key);
		}

		public void removeBindings() {
			bindings.clear();
		}

		public Keymap getResolveParent() {
			return parent;
		}

		public void setResolveParent(Keymap parent) {
			this.parent = parent;
		}

		/**
		 * String representation of the keymap... potentially a very long
		 * string.
		 */
		public String toString() {
			return "Keymap[" + nm + "]" + bindings;
		}

	}
}