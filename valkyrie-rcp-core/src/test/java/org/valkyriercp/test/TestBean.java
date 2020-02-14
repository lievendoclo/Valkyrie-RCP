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
package org.valkyriercp.test;


import java.util.*;

/**
 * @author Oliver Hutchison
 * @author Geoffrey De Smet
 */
public class TestBean {

    private String simpleProperty;

    private Date dateProperty;

    public TestEnum enumProperty;

    private Map mapProperty = new HashMap();

    private List listProperty = new ArrayList();

    private Object[] arrayProperty;

    private Object singleSelectListProperty;

    private TestBean nestedProperty;

    public Object readOnly;

    public Object writeOnly;

    public Number numberProperty;

    private boolean booleanProperty;

    public Number getNumberProperty() {
        return numberProperty;
    }

    public void setNumberProperty(Number numberProperty) {
        this.numberProperty = numberProperty;
    }

    public String getSimpleProperty() {
        return simpleProperty;
    }

    public void setSimpleProperty(String simpleProperty) {
        this.simpleProperty = simpleProperty;
    }

    public Date getDateProperty() {
        return dateProperty;
    }

    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }

    public TestEnum getEnumProperty() {
        return enumProperty;
    }

    public void setEnumProperty(TestEnum enumProperty) {
        this.enumProperty = enumProperty;
    }

    public Map getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map mapProperty) {
        this.mapProperty = mapProperty;
    }

    public List getListProperty() {
        return listProperty;
    }

    public void setListProperty(List listProperty) {
        this.listProperty = listProperty;
    }

    public Object getSingleSelectListProperty() {
        return singleSelectListProperty;
    }

    public void setSingleSelectListProperty(final Object singleSelectListProperty) {
        this.singleSelectListProperty = singleSelectListProperty;
    }

    public TestBean getNestedProperty() {
        return nestedProperty;
    }

    public void setNestedProperty(TestBean nestedProperty) {
        this.nestedProperty = nestedProperty;
    }

    public Object getReadOnly() {
        return readOnly;
    }

    public void setWriteOnly(Object writeOnly) {
        this.writeOnly = writeOnly;
    }

    public Object[] getArrayProperty() {
        return arrayProperty;
    }

    public void setArrayProperty(Object[] arrayProperty) {
        this.arrayProperty = arrayProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

}
