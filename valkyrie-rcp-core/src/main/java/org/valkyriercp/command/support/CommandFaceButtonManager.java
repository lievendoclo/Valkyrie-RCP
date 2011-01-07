package org.valkyriercp.command.support;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.command.config.CommandButtonConfigurer;
import org.valkyriercp.command.config.CommandFaceDescriptor;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class CommandFaceButtonManager implements PropertyChangeListener {
    private Set buttons = new HashSet(6);

    private AbstractCommand command;

    private String faceDescriptorId;

    private CommandFaceDescriptor faceDescriptor;

    private static class ManagedButton {
        private WeakReference buttonHolder;

        private CommandButtonConfigurer buttonConfigurer;

        private int hashCode;

        public ManagedButton(AbstractButton button, CommandButtonConfigurer buttonConfigurer) {
            this.buttonHolder = new WeakReference(button);
            this.buttonConfigurer = buttonConfigurer;
            this.hashCode = button.hashCode();
        }

        public AbstractButton getButton() {
            return (AbstractButton)buttonHolder.get();
        }

        public boolean equals(Object o) {
        	if (o == null) {
        		return false;
        	}
            if (this == o) {
                return true;
            }
            return ObjectUtils.nullSafeEquals(getButton(), ((ManagedButton) o).getButton());
        }

        public int hashCode() {
            return hashCode;
        }
    }

    public CommandFaceButtonManager(AbstractCommand command, String faceDescriptorKey) {
        Assert.notNull(command, "The command to manage buttons for cannot be null");
        Assert.hasText(faceDescriptorKey, "The face descriptor key is required");
        this.command = command;
        this.faceDescriptorId = faceDescriptorKey;
    }

    public CommandFaceButtonManager(AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
        this.command = command;
        setFaceDescriptor(faceDescriptor);
    }

    public void setFaceDescriptor(CommandFaceDescriptor faceDescriptor) {
        Assert.notNull(faceDescriptor, "The face descriptor for managing command button appearance is required");
        if (!ObjectUtils.nullSafeEquals(this.faceDescriptor, faceDescriptor)) {
            if (this.faceDescriptor != null) {
                this.faceDescriptor.removePropertyChangeListener(this);
            }
            this.faceDescriptor = faceDescriptor;
            this.faceDescriptor.addPropertyChangeListener(this);
            propertyChange(null);
        }
    }

    public CommandFaceDescriptor getFaceDescriptor() {
        return faceDescriptor;
    }

    public boolean isFaceConfigured() {
        return this.faceDescriptor != null;
    }

    public void attachAndConfigure(AbstractButton button, CommandButtonConfigurer strategy) {
        Assert.notNull(button, "The button to attach and configure is required");
        Assert.notNull(strategy, "The button configuration strategy is required");
        if (!isAttachedTo(button)) {
            ManagedButton managedButton = new ManagedButton(button, strategy);
            if (buttons.add(managedButton)) {
                configure(button, strategy);
            }
        }
    }

    private void cleanUp() {
        for (Iterator i = buttons.iterator(); i.hasNext();) {
            ManagedButton button = (ManagedButton)i.next();
            if (button.getButton() == null) {
                i.remove();
            }
        }
    }

    protected void configure(AbstractButton button, CommandButtonConfigurer strategy) {
        if (this.faceDescriptor == null) {
            if (command.getFaceDescriptorRegistry() != null) {
                setFaceDescriptor(command.getFaceDescriptorRegistry().getFaceDescriptor(command, faceDescriptorId));
            } else {
                setFaceDescriptor(new CommandFaceDescriptor());
            }
        }

        getFaceDescriptor().configure(button, command, strategy);
    }

    public void detach(AbstractButton button) {
        buttons.remove(findManagedButton(button));
    }

    public void detachAll() {
        buttons.clear();
    }

    public boolean isAttachedTo(AbstractButton button) {
        return findManagedButton(button) != null;
    }

    protected ManagedButton findManagedButton(AbstractButton button) {
        Assert.notNull(button, "The button is required");
        cleanUp();
        for (Iterator i = buttons.iterator(); i.hasNext();) {
            ManagedButton managedButton = (ManagedButton)i.next();
            if (button.equals(managedButton.getButton())) {
                return managedButton;
            }
        }
        return null;
    }

    public Iterator iterator() {
        cleanUp();
        return new ButtonIterator(buttons.iterator());
    }

    private static class ButtonIterator implements Iterator {
        private Iterator it;

        private AbstractButton nextButton;

        public ButtonIterator(Iterator it) {
            this.it = it;
            fetchNextButton();
        }

        public boolean hasNext() {
            return nextButton != null;
        }

        public Object next() {
            if (nextButton == null) {
                throw new NoSuchElementException();
            }
            AbstractButton lastButton = nextButton;
            fetchNextButton();
            return lastButton;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void fetchNextButton() {
            while (it.hasNext()) {
                ManagedButton managedButton = (ManagedButton)it.next();
                nextButton = managedButton.getButton();
                if (nextButton != null) {
                    return;
                }
            }
            nextButton = null;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        Iterator it = buttons.iterator();
        while (it.hasNext()) {
            ManagedButton mb = (ManagedButton)it.next();
            Assert.notNull(mb, "Managed button reference cannot be null");
            if (mb.getButton() == null) {
                it.remove();
            }
            else {
                configure(mb.getButton(), mb.buttonConfigurer);
            }
        }
    }

    public String toString() {
        return new ToStringCreator(this).append("commandId", command.getId())
                .append("faceDescriptor", faceDescriptor)
                .append("attachedButtonCount", buttons.size())
                .toString();
    }
}
