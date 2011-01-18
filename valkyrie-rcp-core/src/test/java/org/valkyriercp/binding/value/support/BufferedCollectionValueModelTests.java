package org.valkyriercp.binding.value.support;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.test.TestPropertyChangeListener;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Test cases for {@link BufferedCollectionValueModel}
 *
 * @author oliverh
 */
public class BufferedCollectionValueModelTests extends AbstractValkyrieTest {

    private Class[] supportedIterfaces = new Class[] {Collection.class, List.class, Set.class, SortedSet.class,};

    private Class[] supportedClasses = new Class[] {ArrayList.class, HashSet.class, TreeSet.class,};

    @Test
    public void testCreating() {
        try {
            getBufferedCollectionValueModel(null, null);
            fail("NULL wrappedType should not be supported");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            getBufferedCollectionValueModel(null, Object.class);
            fail("wrappedType can only be an instance Collection or an array");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            getBufferedCollectionValueModel(null, int[].class);
            fail("wrappedType can not be a primitive array");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        for (int i = 0; i < supportedIterfaces.length; i++) {
            getBufferedCollectionValueModel(null, supportedIterfaces[i]);
        }
        try {
            getBufferedCollectionValueModel(null, CustomCollectionInterface.class);
            fail("if wrappedType is an interface it must one of the standard JDK Collection interfaces");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        getBufferedCollectionValueModel(null, Object[].class);
        getBufferedCollectionValueModel(null, CustomCollectionClass.class);
        for (int i = 0; i < supportedClasses.length; i++) {
            getBufferedCollectionValueModel(null, supportedClasses[i]);
        }
    }

    @Test
    public void testGetAfterBackingObjectChange() {
        Object[] backingArray = getArray(1);
        BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingArray);
        assertHasSameStructure((ListListModel)vm.getValue(), backingArray);

        backingArray = getArray(2);
        vm.getWrappedValueModel().setValue(backingArray);
        assertHasSameStructure((ListListModel)vm.getValue(), backingArray);

        vm.getWrappedValueModel().setValue(null);
        assertEquals("ListListModel must have no elements when backing collection is NULL",
                ((ListListModel)vm.getValue()).size(), 0);

        for (int i = 0; i < supportedClasses.length; i++) {
            Collection backingCollection = getCollection(supportedClasses[i], i);
            vm = getBufferedCollectionValueModel(backingCollection);
            assertHasSameStructure((ListListModel)vm.getValue(), backingCollection);

            backingCollection = getCollection(supportedClasses[i], i + 1);
            vm.getWrappedValueModel().setValue(backingCollection);
            assertHasSameStructure((ListListModel)vm.getValue(), backingCollection);

            vm.getWrappedValueModel().setValue(null);
            assertEquals("ListListModel must have no elements when backing collection is NULL",
                    ((ListListModel)vm.getValue()).size(), 0);
        }
    }

    @Test
    public void testCreateWithEmptyCollection() {
        BufferedCollectionValueModel vm = new BufferedCollectionValueModel(new ValueHolder(null), Collection.class);
        assertTrue(vm.getValue() instanceof ListListModel);
        assertEquals(0, ((ListListModel)vm.getValue()).size());

        vm = new BufferedCollectionValueModel(new ValueHolder(new ArrayList()), Collection.class);
        assertTrue(vm.getValue() instanceof ListListModel);
        assertEquals(0, ((ListListModel)vm.getValue()).size());

        vm = new BufferedCollectionValueModel(new ValueHolder(null), Object[].class);
        assertTrue(vm.getValue() instanceof ListListModel);
        assertEquals(0, ((ListListModel)vm.getValue()).size());

        vm = new BufferedCollectionValueModel(new ValueHolder(new Object[0]), Object[].class);
        assertTrue(vm.getValue() instanceof ListListModel);
        assertEquals(0, ((ListListModel)vm.getValue()).size());
    }

    @Test
    public void testChangesToListListModelWithBackingArray() {
        Object[] backingArray = getArray(100);
        BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingArray);
        ListListModel llm = (ListListModel)vm.getValue();
        llm.clear();
        assertEquals("changes to ListListModel should be not be made to backing array unless commit is called",
                vm.getWrappedValueModel().getValue(), backingArray);

        backingArray = getArray(101);
        vm.getWrappedValueModel().setValue(backingArray);
        Object newValue = new Double(1);
        llm.set(1, newValue);
        vm.commit();
        Object[] newBackingArray = (Object[])vm.getWrappedValueModel().getValue();
        assertNotSame("change should not have been committed back to original array", newBackingArray, backingArray);

        llm.add(newValue);
        vm.commit();
        newBackingArray = (Object[])vm.getWrappedValueModel().getValue();
        assertNotSame("change should not have been committed back to original array", newBackingArray, backingArray);
        assertTrue(newBackingArray.length == backingArray.length + 1);
        assertEquals(newBackingArray[newBackingArray.length - 1], newValue);

        llm.clear();
        vm.commit();
        newBackingArray = (Object[])vm.getWrappedValueModel().getValue();
        assertEquals(newBackingArray.length, 0);

        vm.getWrappedValueModel().setValue(null);
        llm.clear();
        vm.commit();
        assertEquals("if backingCollection is NULL then a commit of an empty LLM should also be NULL",
                vm.getWrappedValueModel().getValue(), null);

        llm.add(newValue);
        vm.commit();
        newBackingArray = (Object[])vm.getWrappedValueModel().getValue();
        assertEquals(newBackingArray.length, 1);
        assertEquals(newBackingArray[0], newValue);
    }

    @Test
    public void testChangesToListListModelWithBackingCollection() {
        for (int i = 0; i < supportedClasses.length; i++) {
            Collection backingCollection = getCollection(supportedClasses[i], 200 + i);
            BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingCollection);
            ListListModel llm = (ListListModel)vm.getValue();
            llm.clear();
            assertEquals("changes to LLM should be not be made to backing collection unless commit is called",
                    vm.getWrappedValueModel().getValue(), backingCollection);

            backingCollection = getCollection(supportedClasses[i], 201 + i);
            vm.getWrappedValueModel().setValue(backingCollection);
            Object newValue = new Integer(-1);
            backingCollection.remove(newValue);
            int orgSize = backingCollection.size();
            llm.set(1, newValue);
            vm.commit();
            Collection newBackingCollection = (Collection)vm.getWrappedValueModel().getValue();
            assertTrue("change should not have been committed back to original array",
                    !backingCollection.contains(newValue));
            assertTrue(newBackingCollection.contains(newValue));
            assertTrue(orgSize == newBackingCollection.size());

            newValue = new Integer(-2);
            backingCollection.remove(newValue);
            orgSize = backingCollection.size();
            llm.add(newValue);
            vm.commit();
            newBackingCollection = (Collection)vm.getWrappedValueModel().getValue();

            assertTrue(newBackingCollection.contains(newValue));
            assertTrue(newBackingCollection.size() == orgSize + 1);

            llm.clear();
            vm.commit();
            assertEquals(((Collection)vm.getWrappedValueModel().getValue()).size(), 0);

            vm.getWrappedValueModel().setValue(null);
            llm.clear();
            vm.commit();
            newBackingCollection = (Collection)vm.getWrappedValueModel().getValue();
            assertEquals("if backingCollection is NULL then a commit of an empty LLM should also be NULL",
                    newBackingCollection, null);

            llm.add(newValue);
            vm.commit();
            newBackingCollection = (Collection)vm.getWrappedValueModel().getValue();
            assertTrue(supportedClasses[i].isAssignableFrom(newBackingCollection.getClass()));
            assertEquals(newBackingCollection.size(), 1);
            assertEquals(newBackingCollection.iterator().next(), newValue);
        }
    }

    @Test
    public void testListListModelKeepsStuctureOfBackingObjectAfterCommit() {
        Collection backingCollection = getCollection(HashSet.class, 500);
        int origLength = backingCollection.size();
        BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingCollection);
        ListListModel llm = (ListListModel)vm.getValue();
        llm.add(backingCollection.iterator().next());
        assertTrue(llm.size() == origLength + 1);
        vm.commit();
        assertTrue("adding a duplicate item should not change the size of a set", llm.size() == origLength);
        assertHasSameStructure(llm, backingCollection);

        backingCollection = getCollection(TreeSet.class, 501);
        vm = getBufferedCollectionValueModel(backingCollection);
        llm = (ListListModel)vm.getValue();
        Collections.reverse(llm);
        assertTrue(((Comparable)llm.get(0)).compareTo(llm.get(1)) > 0);
        vm.commit();
        assertTrue("LLM should be sorted the same way as backingCollection",
                ((Comparable)llm.get(0)).compareTo(llm.get(1)) < 0);
        assertHasSameStructure(llm, backingCollection);

        backingCollection = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Comparable)o2).compareTo(o1);
            }

        });
        populateCollection(backingCollection, 502);
        vm = getBufferedCollectionValueModel(backingCollection);
        llm = (ListListModel)vm.getValue();
        Collections.reverse(llm);
        assertTrue(((Comparable)llm.get(0)).compareTo(llm.get(1)) < 0);
        vm.commit();
        assertTrue("LLM should be sorted the same way as backingCollection",
                ((Comparable)llm.get(0)).compareTo(llm.get(1)) > 0);
        assertHasSameStructure(llm, backingCollection);
    }

    @Test
    public void testIncompatibleCollections() {
        try {
            getBufferedCollectionValueModel(new ArrayList(), Set.class);
            fail("backing object must be assignable to backingCollectionClass");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        catch (BeanCreationException e) {
            if(!(e.getCause() instanceof IllegalArgumentException))
               throw e;
        }
        try {
            getBufferedCollectionValueModel(new Double[0], Integer[].class);
            fail("backing object must be assignable to backingCollectionClass");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
         catch (BeanCreationException e) {
            if(!(e.getCause() instanceof IllegalArgumentException))
               throw e;
        }
    }

    @Test
    public void testValueChangeNotification() {
        Object[] backingArray = getArray(100);
        BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingArray);
        TestPropertyChangeListener vl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        vm.addValueChangeListener(vl);

        ListListModel llm = (ListListModel)vm.getValue();
        assertEquals(0, vl.eventCount());

        vl.reset();
        llm.add(new Integer(100));
        assertEquals(1, vl.eventCount());
        llm.add(1, new Integer(102));
        assertEquals(2, vl.eventCount());

        vl.reset();
        llm.addAll(getCollection(ArrayList.class, 101));
        assertEquals(1, vl.eventCount());
        llm.addAll(1, getCollection(ArrayList.class, 101));
        assertEquals(2, vl.eventCount());

        vl.reset();
        llm.remove(1);
        assertEquals(1, vl.eventCount());
        llm.removeAll(getCollection(ArrayList.class, 101));
        assertEquals(2, vl.eventCount());

        vl.reset();
        llm.set(1, llm.get(1));
        assertEquals(0, vl.eventCount());

        vl.reset();
        llm.clear();
        assertEquals(1, vl.eventCount());
    }

    @Test
    public void testRevert() {
        CommitTrigger commitTriger = new CommitTrigger();

        Collection backingCollection = getCollection(HashSet.class, 700);
        BufferedCollectionValueModel vm = getBufferedCollectionValueModel(backingCollection);
        vm.setCommitTrigger(commitTriger);
        ListListModel llm = (ListListModel)vm.getValue();
        llm.clear();
        commitTriger.revert();
        assertHasSameStructure(llm, backingCollection);
    }

    private void assertHasSameStructure(ListListModel c1, Object[] c2) {
        assertEquals("collections must be the same size", c1.size(), c2.length);
        for (int i = 0; i < c2.length; i++) {
            assertEquals("collections must have the same items in the same order", c1.get(i), c2[i]);
        }
    }

    private void assertHasSameStructure(ListListModel c1, Collection c2) {
        assertEquals("collections must be the same size", c2.size(), c1.size());
        for (Iterator i = c1.iterator(), j = c2.iterator(); i.hasNext();) {
            assertEquals("collections must have the same items in the same order", i.next(), j.next());
        }
    }

    private Object[] getArray(long randomSeed) {
        Random random = new Random(randomSeed);
        return new Number[] {new Integer(random.nextInt()), new Integer(random.nextInt()),
                new Integer(random.nextInt())};
    }

    private Collection getCollection(Class collectionClass, long randomSeed) {
        return populateCollection((Collection) BeanUtils.instantiateClass(collectionClass), randomSeed);
    }

    private Collection populateCollection(Collection c, long randomSeed) {
        Random random = new Random(randomSeed);
        c.add(new Integer(random.nextInt()));
        c.add(new Integer(random.nextInt()));
        c.add(new Integer(random.nextInt()));
        return c;
    }

    private BufferedCollectionValueModel getBufferedCollectionValueModel(Object backingCollecton) {
        return getBufferedCollectionValueModel(backingCollecton, backingCollecton.getClass());
    }

    private BufferedCollectionValueModel getBufferedCollectionValueModel(Object backingCollecton,
            Class backingCollectionClass) {
        ValueModel vm = new ValueHolder(backingCollecton);
        return new BufferedCollectionValueModel(vm, backingCollectionClass);
    }

    interface CustomCollectionInterface extends Collection {

    }

    class CustomCollectionClass extends ArrayList implements CustomCollectionInterface {

    }
}
