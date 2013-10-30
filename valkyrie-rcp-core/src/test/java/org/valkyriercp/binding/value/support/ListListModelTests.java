package org.valkyriercp.binding.value.support;

import junit.framework.TestCase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Testcase for <code>ListListModel</code>
 *
 * @author Keith Donald
 * @author Peter De Bruycker
 */
public class ListListModelTests extends TestCase {

    private ListDataListener mockListener;

    @Before
    public void setUp() throws Exception {
        mockListener = mock(ListDataListener.class);
    }

    @Test
    public void testAddAllCollection() {
        ListListModel model = new ListListModel(Arrays.asList(new Object[]{"1", "2", "3"}));
        model.addListDataListener(mockListener);
        model.addAll(Arrays.asList(new Object[]{"4", "5", "6"}));
        assertEquals(Arrays.asList(new Object[]{"1", "2", "3", "4", "5", "6"}), model);
        verify(mockListener).intervalAdded(any(ListDataEvent.class));
    }

    @Test
    public void testRetainAll() {
        ListListModel model = new ListListModel(Arrays.asList(new Object[]{"1", "2", "3", "4", "5"}));
        model.addListDataListener(mockListener);
        model.retainAll(Arrays.asList(new Object[]{"2", "5"}));
        assertEquals(Arrays.asList(new Object[]{"2", "5"}), model);
        verify(mockListener).contentsChanged(any(ListDataEvent.class));
    }

    @Test
    public void testRemoveAll() {
        ListListModel model = new ListListModel(Arrays.asList(new Object[]{"1", "2", "3", "4", "5"}));
        model.addListDataListener(mockListener);
        model.removeAll(Arrays.asList(new Object[]{"2", "5"}));
        assertEquals(Arrays.asList(new Object[]{"1", "3", "4"}), model);
        verify(mockListener).contentsChanged(any(ListDataEvent.class));
    }

    @Test
    public void testRemove() {
        ListListModel model = new ListListModel(Arrays.asList(new Object[]{"1", "2", "3"}));
        model.addListDataListener(mockListener);
        model.remove(1);
        assertEquals(Arrays.asList(new Object[]{"1", "3"}), model);
        verify(mockListener, atLeastOnce()).intervalRemoved(any(ListDataEvent.class));
    }

    @Test
    public void testRemoveObject() {
        ListListModel model = new ListListModel(Arrays.asList(new Object[]{"1", "2", "3"}));
        model.addListDataListener(mockListener);
        model.remove("2");
        assertEquals(Arrays.asList(new Object[]{"1", "3"}), model);
        verify(mockListener, atLeastOnce()).intervalRemoved(any(ListDataEvent.class));
    }
}

