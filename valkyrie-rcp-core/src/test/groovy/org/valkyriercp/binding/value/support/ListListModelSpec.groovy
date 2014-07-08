package org.valkyriercp.binding.value.support

import spock.lang.Specification

import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class ListListModelSpec extends Specification {
    private ListDataListener mockListener;

    def setup() throws Exception {
        mockListener = Mock(ListDataListener);
    }

    def testAddAllCollection() {
        when:
        ListListModel model = new ListListModel(["1", "2", "3"]);
        model.addListDataListener(mockListener);
        model.addAll(["4", "5", "6"]);
        then:
        model == ["1", "2", "3", "4", "5", "6"]
        1 * mockListener.intervalAdded(_ as ListDataEvent)
    }

    def testRetainAll() {
        when:
        ListListModel model = new ListListModel(["1", "2", "3", "4", "5"]);
        model.addListDataListener(mockListener);
        model.retainAll(["2", "5"]);
        then:
        model == ["2", "5"]
        1 * mockListener.contentsChanged(_ as ListDataEvent)
    }

    def testRemoveAll() {
        when:
        ListListModel model = new ListListModel(["1", "2", "3", "4", "5"]);
        model.addListDataListener(mockListener);
        model.removeAll(["2", "5"]);
        then:
        model == ["1", "3", "4"]
        1 * mockListener.contentsChanged(_ as ListDataEvent)
    }

    def testRemove() {
        when:
        ListListModel model = new ListListModel(["1", "2", "3"]);
        model.addListDataListener(mockListener);
        model.remove(1);
        then:
        model == ["1", "3"]
        1 * mockListener.intervalRemoved(_ as ListDataEvent)
    }

    def testRemoveObject() {
        when:
        ListListModel model = new ListListModel(["1", "2", "3"]);
        model.addListDataListener(mockListener);
        model.remove("2");
        then:
        model == ["1", "3"]
        1 * mockListener.intervalRemoved(_ as ListDataEvent)
    }
}