/*
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